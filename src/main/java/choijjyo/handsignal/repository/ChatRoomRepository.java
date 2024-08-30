package choijjyo.handsignal.repository;

import choijjyo.handsignal.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
}
