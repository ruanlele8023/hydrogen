package com.example.hydrogen.config.client;

import com.example.hydrogen.annotation.ApiServer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ApiServerRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
         this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        var scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ApiServer.class));

        var basePackages = getBasePackages(metadata);

        basePackages.forEach(basePackage -> {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        });

        candidateComponents.forEach(candidateComponent -> {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                var beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                var annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@ApiServer can only be specified on an interface");

                var attributes = annotationMetadata.getAnnotationAttributes(ApiServer.class.getCanonicalName());
                registerApiServer(registry, annotationMetadata, attributes);
            }
        });
    }

    private void registerApiServer(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        var className = annotationMetadata.getClassName();
        Class clazz = ClassUtils.resolveClassName(className, null);

        var beanFactory = registry instanceof ConfigurableBeanFactory ? (ConfigurableBeanFactory) registry: null;
        var contextId = getContextId(beanFactory, attributes);
        var factoryBean = new ApiServerFactoryBean();
        factoryBean.setType(clazz);
        factoryBean.setEnvironment(environment);
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz, () -> {
            try {
                return factoryBean.getObject();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.setLazyInit(true);

        var beanDefinition = definition.getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        beanDefinition.setAttribute("ApiServerFactoryBean", factoryBean);
        beanDefinition.setPrimary(true);

        String[] qualifiers = new String[] { contextId + "ApiServer" };

        var holder = new BeanDefinitionHolder(beanDefinition, className, qualifiers);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getContextId(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        var contextId = (String) attributes.get("contextId");
        if (!StringUtils.hasText(contextId)) {
            return getName(attributes);
        }
        contextId = resolve(beanFactory, contextId);
        return getName(contextId);
    }

    private String getName(Map<String, Object> attributes) {
        return getName(null, attributes);
    }

    private String getName(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = "";
        try {
            String url;
            if (!name.startsWith("http://") &&!name.startsWith("https://")) {
                url = "http://" + name;
            } else {
                url = name;
            }
            host = new URI(url).getHost();
        } catch (URISyntaxException e) {

        }
        Assert.state(host != null , "Service id not legal hostname (" + name + ")");
        return name;
    }

    private String getName(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        var name = (String) attributes.get("serviceId");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("name");
        }
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }
        name = resolve(beanFactory, name);
        return getName(name);
    }

    private String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {
            if (beanFactory == null) {
                return this.environment.resolvePlaceholders(value);
            }
            var resolver = beanFactory.getBeanExpressionResolver();
            var resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            var evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        return Set.of(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
    }
}
