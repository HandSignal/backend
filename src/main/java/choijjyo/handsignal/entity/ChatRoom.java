package choijjyo.handsignal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ChatRoom {

    @Id
    private String id = UUID.randomUUID().toString();  // UUID를 사용해 방 ID 생성

    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntry> entries;
}
