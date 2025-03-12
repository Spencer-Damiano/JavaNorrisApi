package com.spencer;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

public record JokeRecord(
        List<String> categories,
        LocalDateTime createdAt,
        String iconUrl,
        String id,
        LocalDateTime updatedAt,
        URI url,
        String value
) {

}
