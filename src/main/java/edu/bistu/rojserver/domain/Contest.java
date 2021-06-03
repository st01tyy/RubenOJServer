package edu.bistu.rojserver.domain;

import edu.bistu.rojserver.dao.entity.ContestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contest
{
    private Long contestID;
    private String title;
    private String password;
    private String startTime;
    private String startTimeCalendar;
    private String endTime;
    private String endTimeCalendar;
    private Long userID;

    public Contest(ContestEntity contestEntity)
    {
        this.contestID = contestEntity.getContestID();
        this.title = contestEntity.getTitle();
        this.password = contestEntity.getPassword();
        this.startTime = contestEntity.getStartTime();
        this.endTime = contestEntity.getEndTime();
        this.userID = contestEntity.getUserEntity().getUserID();
    }
}
