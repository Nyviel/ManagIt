package res.managit.dbo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import res.managit.dbo.entity.EventItem;

@Dao
public interface EventItemDao {

    @Query("Select * from EventItem")
    public List<EventItem> getAll();

    @Query("Select * from EventItem where eventItemId like :id")
    public EventItem getEventItemById(Long id);

    @Query("Select * from EventItem where amount like :amount")
    public List<EventItem> getEventItemByAmount(int amount);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEventItem(EventItem... eventItems);

    @Delete
    void deleteEventItem(EventItem... eventItems);

    @Query("Delete from EventItem")
    void deleteAll();

    @Update
    void updateEventItem(EventItem eventItem);


}
