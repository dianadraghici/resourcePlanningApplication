package dania.app.web.controllers;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.errorHandler.BadRequestException;
import dania.app.web.service.MemberService;
import dania.app.web.utils.Constants;
import dania.app.web.utils.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static dania.app.web.utils.Cleaner.processGivenPageParams;

@RestController
@RequestMapping(Constants.MEMBER_CONTROLLER)
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberDTO> findAllMembers() {
        return memberService.findAll();
    }

    /**
     * @param page   - the page number to retrieve.
     * @param size   - the maxim size or length of elements to be on the given page.
     * @param sortBy - the sort criteria to be apply on the query made.
     * @param direction - the direction [ASC/DESC] to be apply on the query made.
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     */
    @GetMapping(value = Constants.FIND_MEMBER_PAGINATED)
    public Pair<List<MemberDTO>, Long> findMembersPage(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String sortBy, @RequestParam String direction) {
        final PageParams params = processGivenPageParams(new PageParams(page, size, sortBy, Sort.Direction.fromString(direction)), MemberDTO.class);
        return memberService.findAllWith(PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getSortBy())));
    }

    /**
     * @param page - the page number to retrieve.
     * @param size - the maxim size or length of elements to be on the given page.
     * @param sortBy - the sort criteria to be apply on the query made.
     * @param direction - the direction [ASC/DESC] to be apply on the query made.
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     */
    @GetMapping(path = Constants.FIND_MEMBER_BY_FLAG_PAGINATED)
    public Pair<List<MemberDTO>, Long> findMembersPageByFlag(@PathVariable byte flag, @RequestParam Integer page, @RequestParam Integer size, @RequestParam String sortBy, @RequestParam String direction) {
        final PageParams params = processGivenPageParams(new PageParams(page, size, sortBy, Sort.Direction.fromString(direction)), MemberDTO.class);
        return memberService.findAllBy(flag, PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getSortBy())));
    }

    @PostMapping
    public MemberDTO create(@RequestBody @Valid MemberDTO memberDTO) {
        return memberService.create(memberDTO);
    }

    @PutMapping(path = Constants.UPDATE_MEMBER)
    public void update(@RequestBody @Valid MemberDTO memberDTO) {
        if (memberDTO.getId() == null || memberDTO.getId() <= 0) {
            throw new BadRequestException("Member - Member Id must be a positive number!");
        }
        memberService.update(memberDTO);
    }

    @DeleteMapping(path = Constants.DELETE_MEMBER)
    public void delete(@PathVariable Integer id) {
        memberService.delete(id);
    }

    @PutMapping(path = Constants.DEACTIVATE_MEMBER)
    public MemberDTO deactivate(@RequestBody Integer id) {
        return memberService.deactivateMember(id);
    }

    @GetMapping(path = Constants.GET_INACTIVE_MEMBERS)
    public List<MemberDTO> getInactiveMembers() {
        return memberService.findAllByInactiveFlag();
    }

    @GetMapping(path = Constants.GET_ACTIVE_MEMBERS)
    public List<MemberDTO> getActiveMembers() {
        return memberService.findAllByActiveFlag();
    }

    /**
     * Search a member by given staff number
     *
     * @param staffNumber - staff number.
     * @return the project with given staff number
     */
    @GetMapping(path = Constants.FIND_MEMBER_BY_STAFF_NUMBER)
    public MemberDTO getMemberByStaffNumber(@PathVariable String staffNumber) {
        return memberService.findByStaffNumber(staffNumber);
    }
}
