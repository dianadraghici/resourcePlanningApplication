package dania.app.web.service;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.entities.CalendarEntity;
import dania.app.web.entities.MemberEntity;
import dania.app.web.mapper.MemberMapper;
import dania.app.web.repository.CalendarRepository;
import dania.app.web.repository.MemberPositionRepository;
import dania.app.web.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
public class MemberService implements ServiceManager<MemberDTO, Integer> {

    private MemberRepository memberRepository;
    private MemberMapper memberMapper;
    private ParameterService parameterService;
    private MemberPositionRepository memberPositionRepository;
    private CalendarRepository calendarRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper, ParameterService parameterService,
                         MemberPositionRepository memberPositionRepository, CalendarRepository calendarRepository) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.parameterService = parameterService;
        this.memberPositionRepository = memberPositionRepository;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public List<MemberDTO> findAll() {
        List<MemberDTO> memberDTOList = memberMapper.listMemberEntityToDTO(
                memberRepository.findAll());
        setTechnologyDescriptionForMemberDTOSList(memberDTOList);

        return memberDTOList;
    }

    /**
     * Returns a member Object given an id
     *
     * @param id mandatory id in order to retrieve a specific member from DB
     * @return a Member Object
     */
    @Override
    public MemberDTO findById(Integer id) {
        return memberMapper.memberEntityToDTO(memberRepository.findById(id));
    }

    @Override
    public MemberDTO delete(Integer id) {
        MemberDTO memberDTO = findById(id);
        if (memberDTO != null) {
            memberRepository.delete(memberMapper.memberDTOToEntity(memberDTO));
        }
        return memberDTO;
    }



    /**
     * @param pageable - the pageable obj which will be used to perform the pagination
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     * @see org.springframework.data.domain.Pageable
     */
    public Pair<List<MemberDTO>, Long> findAllWith(Pageable pageable) {
        List<MemberDTO> memberDTOList = memberRepository.findAll(pageable).getContent().stream()
                .filter(memberEntity -> memberEntity.getTechnologyId() >= 0)
                .map(memberMapper::memberEntityToDTO)
                .map(this::setTechnologyDescriptionForMemberDTO)
                .collect(toList());
        return Pair.of(memberDTOList, memberRepository.count());
    }

    /**
     * @param flag - the (active/inactive) flag to be apply on the query
     * @param pageable - the pageable obj which will be used to perform the pagination
     * @return a Pair containing first the retrieved page requested and secondly the total size of elements.
     * @see org.springframework.data.domain.Pageable
     */
    public Pair<List<MemberDTO>, Long> findAllBy(final byte flag, Pageable pageable) {
        List<MemberDTO> memberDTOList = memberRepository.findAllByFlagEquals(flag, pageable).getContent().stream()
                .map(memberMapper::memberEntityToDTO)
                .map(this::setTechnologyDescriptionForMemberDTO)
                .collect(toList());
        return Pair.of(memberDTOList, memberRepository.countAllByFlagEquals(flag));
    }



    @Override
    public MemberDTO create(MemberDTO memberDTO) {
        return memberMapper.memberEntityToDTO(memberRepository.saveAndFlush(memberMapper.memberDTOToEntity(memberDTO)));
    }

    @Override
    public MemberDTO update(MemberDTO memberDTO) {
        return memberMapper.memberEntityToDTO(memberRepository.saveAndFlush(memberMapper.memberDTOToEntity(memberDTO)));
    }

    public MemberDTO findByStaffNumber(String staffNumber) {
        return memberMapper.memberEntityToDTO(memberRepository.findByStaffNumber(staffNumber));
    }

    public List<MemberDTO> findAllByActiveFlag() {
        return findAllBy((byte) 1, null).getFirst();
    }

    public List<MemberDTO> findAllByInactiveFlag() {
        return findAllBy((byte) 0, null).getFirst();
    }

    /**
     * @param memberId - the id of the memberEntity to deactivate
     * @return the deactivated member as DTO
     */
    @Transactional
    public MemberDTO deactivateMember(int memberId) {
        MemberEntity memberEntity = Optional.ofNullable(memberRepository.findById(memberId))
                .orElseThrow(() -> new RuntimeException("No MemberEntity found with the given id"));
        setDisableFlagOnGivenMemberEntity(memberEntity, memberRepository);
        CalendarEntity currentEop = calendarRepository.findFirstByEopGreaterThanEqual(LocalDate.now())
                .orElseThrow(() -> new RuntimeException(format("No valid EOP Calendar found for Today [%s]", LocalDate.now())));
        setCurrentEopOnMemberPositionCollection(memberId, currentEop, memberPositionRepository);
        return findById(memberId);
    }

    private void setTechnologyDescriptionForMemberDTOSList(List<MemberDTO> memberDTOList) {
        for (MemberDTO memberDTO : memberDTOList) {
            if (memberDTO.getTechnologyId() >= 0) {
                memberDTO.setTechnologyDescription(parameterService.findByIdTechnology(memberDTO.getTechnologyId()).getDescription());
            }
        }
    }

    /**
     * @param dto - the MemberDTO instance on which the Technology Description will be set.
     * @return the given MemberDTO instance.
     */
    private MemberDTO setTechnologyDescriptionForMemberDTO(MemberDTO dto) {
        Optional.ofNullable(dto)
                .filter(memberDTO -> memberDTO.getTechnologyId() >= 0)
                .ifPresent(memberDTO -> memberDTO.setTechnologyDescription(parameterService.findByIdTechnology(memberDTO.getTechnologyId()).getDescription()));
        return dto;
    }

    /**
     * @param memberId           - the memberEntity's id to retrieve his MemberPositionEntity's Collection
     * @param calendarEntity     - the CalendarEntity instance to be set as new endDateCalendarEntity on each element of the MemberPositionEntity's Collection
     * @param memberPositionRepo - the MemberPositionRepository instance to perform the DAO operation
     */
    private void setCurrentEopOnMemberPositionCollection(int memberId, CalendarEntity calendarEntity, MemberPositionRepository memberPositionRepo) {
        memberPositionRepo.findMemberPositionEntityByMemberEntity_Id(memberId)
                .stream()
                .peek(memberPositionEntity -> memberPositionEntity.setEndDateCalendarEntity(calendarEntity))
                .forEach(memberPositionRepo::save);
    }

    /**
     * @param memberEntity - the memberEntity on which the active flag will be disable
     * @param memberRepo   - the MemberRepository instance to perform the DAO operation
     */
    private void setDisableFlagOnGivenMemberEntity(MemberEntity memberEntity, MemberRepository memberRepo) {
        memberEntity.setFlag((byte) 0);
        memberRepo.save(memberEntity);
    }
}