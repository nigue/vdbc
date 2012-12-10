package org.indp.vdbc.db;

import org.indp.vdbc.db.impl.GenericDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class DialectSupport {
    private static final Logger LOG = LoggerFactory.getLogger(DialectSupport.class);

    private static final Map<String, DialectItem> DIALECTS = new LinkedHashMap<String, DialectItem>();

    static {
        add(new DialectItem("generic", "Generic", GenericDialect.class));
//        add(new DialectItem("derby", "Derby", ));
    }

    public static Dialect getDialect(String id) {
        try {
            return DIALECTS.containsKey(id)
                    ? DIALECTS.get(id).dialectClass.newInstance()
                    : null;
        } catch (InstantiationException e) {
            LOG.error(e.getMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public static Collection<DialectItem> getDialectTypes() {
        return DIALECTS.values();
    }

    public static DialectItem getDialectType(String id) {
        return DIALECTS.get(id);
    }

    public static Collection<String> getDialectCodes() {
        return DIALECTS.keySet();
    }

    public static Dialect getGenericDialect() {
        return getDialect("generic");
    }

    private static void add(DialectItem item) {
        DIALECTS.put(item.id, item);
    }

    private DialectSupport() {
    }

    public static class DialectItem {
        private String id;
        private String title;
        private Class<? extends Dialect> dialectClass;

        private DialectItem(String id, String title, Class<? extends Dialect> dialectClass) {
            this.id = id;
            this.title = title;
            this.dialectClass = dialectClass;
        }

        public String getTitle() {
            return title;
        }
    }
}