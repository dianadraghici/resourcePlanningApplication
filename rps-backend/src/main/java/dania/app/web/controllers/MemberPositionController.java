package dania.app.web.controllers;

import dania.app.web.controllers.dto.MemberPositionDTO;
import dania.app.web.errorHandler.BadRequestException;
import dania.app.web.service.MemberPositionService;
import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Constants.MEMBER_POSITION_CONTROLLER)
@Validated
public class MemberPositionController {

    private MemberPositionService memberPositionService;

    @Autowired
    public MemberPositionController(MemberPositionService memberPositionService) {
        this.memberPositionService = memberPositionService;
    }

    @GetMapping(path = Constants.FIND_BY_ID_MEMBER_POSITION)
    public List<MemberPositionDTO> findProjectPositionByMemberId(@PathVariable Integer id) {
        return new ArrayList<>(memberPositionService.findMemberPositionByIdMember(id));
    }

    @PostMapping
    public MemberPositionDTO create(@RequestBody @Valid MemberPositionDTO memberPositionDTO) {
        return memberPositionService.create(memberPositionDTO);
    }

    @PutMapping(path = Constants.UPDATE_MEMBER_POSITION)
    public MemberPositionDTO update(@RequestBody @Valid MemberPositionDTO memberPositionDTO) {
        if (memberPositionDTO.getId() == null || memberPositionDTO.getId() <= 0) {
            throw new BadRequestException("Member Position - Member Position Id must be a positive number!");
        }
        return memberPositionService.update(memberPositionDTO);
    }

    /**
     * Delete Mapping for deleting a MemberPositionEntity
     */
    @DeleteMapping(path = Constants.DELETE_MEMBER_POSITION)
    public void delete(@PathVariable int id) {
        memberPositionService.delete(id);
    }
}

