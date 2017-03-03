package com.taobao.weex;

import android.content.Context;
import android.content.res.AssetManager;

import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.SimpleComponentHolder;
import com.taobao.weex.utils.WXLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Weex plugin Container
 * Created by moxun on 17/3/3.
 */

public class WeexPluginContainer {
    public static void loadModules(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            String[] modulePlugins = assetManager.list("weex_plugin/module");
            if (modulePlugins != null && modulePlugins.length > 0) {
                for (String module : modulePlugins) {
                    if (module.endsWith(".properties")) {
                        Properties properties = new Properties();
                        InputStream inputStream = assetManager.open("weex_plugin/module/" + module);
                        properties.load(inputStream);
                        inputStream.close();
                        try {
                            Class clazz = Class.forName(properties.getProperty("class"));
                            WXSDKEngine.registerModule(properties.getProperty("name"), clazz, Boolean.parseBoolean(properties.getProperty("globalRegistration")));
                            WXLogUtils.e("REGISTER_PLUGIN", "Module " + properties.getProperty("class") + " registered");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (WXException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadComponents(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            String[] componentPlugins = assetManager.list("weex_plugin/component");
            if (componentPlugins != null && componentPlugins.length > 0) {
                for (String component : componentPlugins) {
                    if (component.endsWith(".properties")) {
                        Properties properties = new Properties();
                        InputStream inputStream = assetManager.open("weex_plugin/module/" + component);
                        properties.load(inputStream);
                        inputStream.close();

                        try {
                            Class clazz = Class.forName(properties.getProperty("class"));
                            boolean appendTree = Boolean.parseBoolean(properties.getProperty("appendTree"));
                            String[] names = properties.getProperty("names").split(",");
                            if (Boolean.parseBoolean(properties.getProperty("usingHolder"))) {
                                String creator = properties.getProperty("creator");
                                if ("NULL".equals(creator)) {
                                    WXSDKEngine.registerComponent(new SimpleComponentHolder(clazz), appendTree, names);
                                } else {
                                    Class creatorClass = Class.forName(creator);
                                    ComponentCreator componentCreator = (ComponentCreator) creatorClass.newInstance();
                                    WXSDKEngine.registerComponent(new SimpleComponentHolder(clazz, componentCreator), appendTree, names);
                                }
                            } else {
                                WXSDKEngine.registerComponent(clazz, appendTree, names);
                            }
                            WXLogUtils.e("REGISTER_PLUGIN", "Component " + properties.getProperty("class") + " registered");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        } catch (WXException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDomObject(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            String[] domObjectPlugins = assetManager.list("weex_plugin/dom");
            if (domObjectPlugins != null && domObjectPlugins.length > 0) {
                for (String domObject : domObjectPlugins) {
                    if (domObject.endsWith(".properties")) {
                        Properties properties = new Properties();
                        InputStream inputStream = assetManager.open("weex_plugin/dom/" + domObject);
                        properties.load(inputStream);
                        inputStream.close();
                        try {
                            Class clazz = Class.forName(properties.getProperty("class"));
                            WXSDKEngine.registerDomObject(properties.getProperty("type"), clazz);
                            WXLogUtils.e("REGISTER_PLUGIN", "DomObject " + properties.getProperty("class") + " registered");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (WXException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
