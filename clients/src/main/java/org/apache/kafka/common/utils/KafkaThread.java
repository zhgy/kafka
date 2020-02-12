/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper for Thread that sets things up nicely
 */
public class KafkaThread extends Thread {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    public static KafkaThread daemon(final String name, Runnable runnable) {
        return new KafkaThread(name, runnable, true);
    }

    public static KafkaThread nonDaemon(final String name, Runnable runnable) {
        return new KafkaThread(name, runnable, false);
    }

    public KafkaThread(final String name, boolean daemon) {
        super(name);
        configureThread(name, daemon);
    }

    public KafkaThread(final String name, Runnable runnable, boolean daemon) {
        super(runnable, name);
        configureThread(name, daemon);
    }

    private void configureThread(final String name, boolean daemon) {
        // JVM会在没有没有用户线程时退出。KafkaClient作为一个库，当然不能阻止JVM推出
        // 对于线程里未处理的异常，也要记录后吞掉。不能让用户应用挂掉
        setDaemon(daemon);
        setUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception in thread '{}':", name, e));
    }

}
