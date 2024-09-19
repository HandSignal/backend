package choijjyo.handsignal.videoCall.repository;

import choijjyo.handsignal.videoCall.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
}
