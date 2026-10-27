package com.soccorsoweb.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.ServletContext;


public class FreeMarkerTemplateManager {

    private static Configuration cfg;

    public static void setup(ServletContext servletContext) {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setServletContextForTemplateLoading(servletContext, "/WEB-INF/templates");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER); // Better for development
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
        }
    }

    public static Configuration getConfiguration() {
        if (cfg == null) {
            throw new IllegalStateException("FreeMarkerTemplateManager has not been initialized. Call setup() first.");
        }
        return cfg;
    }
}
