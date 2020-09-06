package stream.api;

import common.test.tool.annotation.Easy;
import common.test.tool.dataset.ClassicOnlineStore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WhatIDidNotKnowAboutTest extends ClassicOnlineStore {
    Map<Long, Long> count(Map<String, SomeThingHasCount>... visits) {
        return Arrays.stream(visits).flatMap(map -> map.entrySet().stream())
                .filter(entry -> {
                    String key = entry.getKey();
                    try {
                        Long.parseLong(key);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.groupingBy(
                        entry -> Long.parseLong(entry.getKey()),
                        Collectors.summingLong(entry -> entry.getValue().getCount().orElse(0L))
                ));
    }

    @Easy @Test
    public void testCount() {
        Map<String, SomeThingHasCount> group1 = Map.of(
                "123", new SomeThingHasCount(Optional.of(100L)),
                "456", new SomeThingHasCount(Optional.empty())
        );
        Map<String, SomeThingHasCount> group2 = Map.of(
                "123", new SomeThingHasCount(Optional.of(200L)),
                "46", new SomeThingHasCount(Optional.of(1L)),
                "ABC", new SomeThingHasCount(Optional.of(1L))
        );
        Map<Long, Long> result = count(group1, group2);
        assertThat(result.get(123L), is(300L));
        assertThat(result.get(456L), is(0L));
        assertThat(result.get(46L), is(1L));
    }
}
