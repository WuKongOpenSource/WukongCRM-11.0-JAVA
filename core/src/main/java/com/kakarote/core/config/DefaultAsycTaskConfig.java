package com.kakarote.core.config;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync(proxyTargetClass = true)
@Configuration
public class DefaultAsycTaskConfig {

    /**
     *  线程池维护线程的最小数量.
     */
    @Value("${asyc-task.corePoolSize:10}")
    private int corePoolSize;
    /**
     *  线程池维护线程的最大数量
     */
    @Value("${asyc-task.maxPoolSize:200}")
    private int maxPoolSize;
    /**
     *  队列最大长度
     */
    @Value("${asyc-task.queueCapacity:10}")
    private int queueCapacity;
    /**
     *  线程池前缀
     */
    @Value("${asyc-task.threadNamePrefix:kakarote-}")
    private String threadNamePrefix;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
        @Override
        public void execute(@NotNull Runnable runnable) {
            Runnable ttlRunnable = TtlRunnable.get(runnable);
            super.execute(ttlRunnable);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            Callable ttlCallable = TtlCallable.get(task);
            return super.submit(ttlCallable);
        }

        @Override
        public Future<?> submit(Runnable task) {
            Runnable ttlRunnable = TtlRunnable.get(task);
            return super.submit(ttlRunnable);
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable task) {
            Runnable ttlRunnable = TtlRunnable.get(task);
            return super.submitListenable(ttlRunnable);
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            Callable ttlCallable = TtlCallable.get(task);
            return super.submitListenable(ttlCallable);
        }
    }
}
