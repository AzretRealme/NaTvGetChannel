package NaTV.Main.dao;

import NaTV.Main.models.entity.Channel;
import NaTV.Main.models.objects.OutputChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepo extends JpaRepository<Channel, Long> {

}
