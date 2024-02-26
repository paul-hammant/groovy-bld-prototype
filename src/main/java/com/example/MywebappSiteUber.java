package com.example;

import rife.engine.Server;

public class MywebappSiteUber extends MywebappSite {
    public static void main(String[] args) {
        new Server()
            .staticUberJarResourceBase("webapp")
            .start(new MywebappSiteUber());
    }
}