package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.ProblemStatus;
import edu.bistu.rojserver.dao.entity.ContestEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.ContestRepository;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.RankTableItem;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.Contest;
import edu.bistu.rojserver.domain.ProblemSelectForm;
import edu.bistu.rojserver.domain.UserContestTableItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ContestService
{
    @Resource
    private ContestRepository contestRepository;

    @Resource
    private ProblemRepository problemRepository;

    @Resource
    private UserRepository userRepository;

    public List<Contest> getContestList()
    {
        List<ContestEntity> entityList = contestRepository.getContestEntityList();
        if(entityList == null || entityList.size() == 0)
            return null;
        List<Contest> contestList = new ArrayList<>(entityList.size());
        for(ContestEntity contestEntity : entityList)
        {
            Contest contest = new Contest(contestEntity);
            contest.setStartTimeCalendar(LocalDateTime.parse(contestEntity.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh点mm分")));
            contest.setEndTimeCalendar(LocalDateTime.parse(contestEntity.getEndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh点mm分")));
            contestList.add(contest);
        }
        return contestList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long editContestInformation(Contest contest, UserEntity userEntity) throws ParseException
    {
        ContestEntity contestEntity;
        if(contest.getContestID() == null)
        {
            contestEntity = new ContestEntity();
            contestEntity.setUserEntity(userEntity);
        }
        else
            contestEntity = contestRepository.getByContestID(contest.getContestID());

        contestEntity.setTitle(contest.getTitle());
        contestEntity.setPassword(contest.getPassword());

        LocalDateTime start = LocalDateTime.parse(contest.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(contest.getEndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if(start.isAfter(end))
        {
            contestEntity.setStartTime(contest.getEndTime());
            contestEntity.setEndTime(contest.getStartTime());
        }
        else
        {
            contestEntity.setStartTime(contest.getStartTime());
            contestEntity.setEndTime(contest.getEndTime());
        }

        contestEntity = contestRepository.saveAndFlush(contestEntity);
        return contestEntity.getContestID();
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProblemsToContest(ProblemSelectForm form)
    {
        ContestEntity contestEntity = contestRepository.getByContestID(form.getContestID());
        for(Long problemID : form.getProblems())
        {
            ProblemEntity problemEntity = problemRepository.getByProblemID(problemID);
            problemEntity.setProblemStatus(ProblemStatus.IN_CONTEST);
            problemEntity.setContestEntity(contestEntity);
            problemRepository.save(problemEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProblemFromContest(ProblemSelectForm form)
    {
        ProblemEntity problemEntity = problemRepository.getByProblemID(form.getProblemID());
        problemEntity.setProblemStatus(ProblemStatus.READY);
        problemEntity.setContestEntity(null);
        problemRepository.saveAndFlush(problemEntity);
    }

    public Contest getContest(Long contestID)
    {
        ContestEntity contestEntity = contestRepository.findByContestID(contestID).orElse(null);
        if(contestEntity == null)
            return new Contest();
        return new Contest(contestEntity);
    }

    public List<UserContestTableItem> getContestRankTable(Long contestID)
    {
        List<Long> contestants = contestRepository.getContestants(contestID);
        if(contestants == null || contestants.size() == 0)
            return new ArrayList<>();
        List<UserContestTableItem> res = new ArrayList<>();
        for(Long userID : contestants)
        {
            UserEntity userEntity = userRepository.getByUserID(userID);
            RankTableItem rankTableItem = contestRepository.getContestantInformation(userID, contestID);
            UserContestTableItem item = new UserContestTableItem();
            item.setUsername(userEntity.getUsername());
            item.setScore((rankTableItem.getTotalScore() == null ? 0 : rankTableItem.getTotalScore()));
            item.setPassNumber(rankTableItem.getPassNumber());
            res.add(item);
        }
        res.sort((o1, o2) -> {
            if (o1.getScore() > o2.getScore())
                return -1;
            else if (o1.getScore().equals(o2.getScore()))
                return 0;
            else
                return 1;
        });
        return res;
    }
}
