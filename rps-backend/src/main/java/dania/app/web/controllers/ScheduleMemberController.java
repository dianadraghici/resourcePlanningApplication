package dania.app.web.controllers;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.service.MemberPositionViewService;
import dania.app.web.utils.Constants;
import dania.app.web.utils.Cleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@RestController
@RequestMapping({Constants.SCHEDULE_MEMBER_CONTROLLER})
public class ScheduleMemberController {

    private MemberPositionViewService memberPositionViewService;

    @Autowired
    public ScheduleMemberController(MemberPositionViewService memberPositionViewService) {
        this.memberPositionViewService = memberPositionViewService;
    }

    @GetMapping
    @RequestMapping({Constants.GET_MAP_POSITIONS_BY_MEMBER})
    public Map<String, List<MemberPositionViewDTO>> getMapPositionsByMember() {
        return Cleaner.getSortedMap(memberPositionViewService.findAll().stream()
                .collect(groupingBy(MemberPositionViewDTO::concatFirstLastName)));
    }

}
