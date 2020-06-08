package dania.app.web.service;

import dania.app.web.controllers.dto.MemberPositionDTO;
import dania.app.web.mapper.MemberPositionMapper;
import dania.app.web.repository.MemberPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberPositionService implements ServiceManager<MemberPositionDTO, Integer> {

    private final MemberPositionRepository memberPositionRepository;
    private final MemberPositionMapper memberPositionMapper;
    private final ParameterService parameterService;

    @Autowired
    public MemberPositionService(MemberPositionRepository memberPositionRepository,
                                 MemberPositionMapper memberPositionMapper,
                                 ParameterService parameterService) {
        this.memberPositionRepository = memberPositionRepository;
        this.memberPositionMapper = memberPositionMapper;
        this.parameterService = parameterService;
    }

    @Override
    public MemberPositionDTO create(MemberPositionDTO memberPositionDTO) {
        return memberPositionMapper.memberPositionEntityToDTO(
                memberPositionRepository.saveAndFlush(
                        memberPositionMapper.memberPositionDTOToEntity(memberPositionDTO)));
    }

    @Override
    public MemberPositionDTO delete(Integer id) {
        MemberPositionDTO member = findById(id);
        if (member != null) {
            memberPositionRepository.delete(memberPositionMapper.memberPositionDTOToEntity(member));
        }
        return member;
    }

    @Override
    public MemberPositionDTO update(MemberPositionDTO memberPositionDTO) {
        return memberPositionMapper.memberPositionEntityToDTO(
                memberPositionRepository.saveAndFlush(
                        memberPositionMapper.memberPositionDTOToEntity(memberPositionDTO)));
    }

    @Override
    public List<MemberPositionDTO> findAll() {
        List<MemberPositionDTO> memberPositionDTOList = memberPositionMapper.listMemberPositionEntityToDTO(
                memberPositionRepository.findAll());
        setMemberPositionDTOSList(memberPositionDTOList);

        return memberPositionDTOList;
    }

    @Override
    public MemberPositionDTO findById(Integer id) {
        return memberPositionMapper.memberPositionEntityToDTO(
                memberPositionRepository.findMemberPositionEntityById(id));
    }

    public List<MemberPositionDTO> findMemberPositionByIdMember(int id) {
        List<MemberPositionDTO> memberPositionDTOList = memberPositionMapper.listMemberPositionEntityToDTO(
                memberPositionRepository.findMemberPositionEntityByMemberEntity_Id(id));
        setMemberPositionDTOSList(memberPositionDTOList);

        return memberPositionDTOList;
    }

    public List<MemberPositionDTO> findMemberPositionByIdProjectPosition(int id) {
        List<MemberPositionDTO> memberPositionDTOList = memberPositionMapper.listMemberPositionEntityToDTO(
                memberPositionRepository.findMemberPositionEntityByProjectPositionEntity_Id(id));
        setMemberPositionDTOSList(memberPositionDTOList);

        return memberPositionDTOList;
    }

    private void setMemberPositionDTOSList(List<MemberPositionDTO> memberPositionDTOList) {
        for (MemberPositionDTO memberPositionDTO : memberPositionDTOList) {
            if (memberPositionDTO.getProjectPositionDTO() != null) {
                if (memberPositionDTO.getProjectPositionDTO().getPositionId() >= 0) {
                    memberPositionDTO.getProjectPositionDTO().setPositionDescription(parameterService.findByIdPosition(
                            memberPositionDTO.getProjectPositionDTO().getPositionId()).getDescription());
            }
                if (memberPositionDTO.getProjectPositionDTO().getPercentId() >= 0) {
                    memberPositionDTO.getProjectPositionDTO().setPercentDescription(parameterService.findByIdPercent(
                            memberPositionDTO.getProjectPositionDTO().getPercentId()).getDescription());
                }
            }

            memberPositionDTO.getMemberDTO().setTechnologyDescription(parameterService.findByIdTechnology(
                    memberPositionDTO.getMemberDTO().getTechnologyId()).getDescription());
            memberPositionDTO.setPercentDescription(parameterService.findByIdPercent(
                    memberPositionDTO.getPercentId()).getDescription());
            memberPositionDTO.getProjectPositionDTO().getProjectDTO().setStatusDescription(parameterService.
                    findByIdStatus(memberPositionDTO.getProjectPositionDTO().getProjectDTO().getStatusId()).
                    getDescription());
            memberPositionDTO.getProjectPositionDTO().getProjectDTO().setPercentDescription(parameterService.
                    findByIdPercent(memberPositionDTO.getProjectPositionDTO().getProjectDTO().getPercentId()).
                    getDescription());

        }
    }
}
