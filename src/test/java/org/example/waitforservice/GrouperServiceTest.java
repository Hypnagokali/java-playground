package org.example.waitforservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GrouperServiceTest {


    @SuppressWarnings({ "resource" })
    @Test
    void shouldOnlyStart4GroupingTasksAtOnce() throws InterruptedException {
        GrouperService grouperService = new GrouperService( new Grouper() );
        ExecutorService executorService = Executors.newFixedThreadPool( 100 );

        for ( int i = 0; i < 10; i++ ) {
            final int next = i;
            executorService.submit( () -> grouperService.importDataAndGroup( next ) );
        }
        executorService.shutdown();

        assertThat(executorService.awaitTermination( 10, TimeUnit.SECONDS )).isTrue();
    }
}