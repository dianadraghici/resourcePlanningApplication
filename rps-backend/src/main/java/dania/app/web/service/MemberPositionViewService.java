package dania.app.web.service;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.mapper.MemberPositionViewMapper;
import dania.app.web.repository.MemberPositionViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberPositionViewService implements ServiceManager<MemberPositionViewDTO, String> {

    private MemberPositionViewRepository memberPositionViewRepository;
    private MemberPositionViewMapper memberPositionViewMapper;

    @Autowired
    public MemberPositionViewService(MemberPositionViewRepository memberPositionViewRepository,
                                     MemberPositionViewMapper memberPositionViewMapper) {
        this.memberPositionViewRepository = memberPositionViewRepository;
        this.memberPositionViewMapper = memberPositionViewMapper;
    }

    @Override
    public MemberPositionViewDTO create(MemberPositionViewDTO memberPositionViewDTO) {
        return memberPositionViewMapper.memberPositionViewEntityToDTO(
                memberPositionViewRepository.saveAndFlush(
                        memberPositionViewMapper.memberPositionViewDTOToEntity(memberPositionViewDTO)));
    }

    @Override
    public MemberPositionViewDTO delete(String id) {
        MemberPositionViewDTO memberPositionViewDTO = findById(id);
        if (memberPositionViewDTO != null) {
            memberPositionViewRepository.delete(
                    memberPositionViewMapper.memberPositionViewDTOToEntity(memberPositionViewDTO));
        }
        return memberPositionViewDTO;
    }

    @Override
    public List<MemberPositionViewDTO> findAll() {
        return memberPositionViewMapper.listMemberPositionViewEntityToDTO(memberPositionViewRepository.findAll());
    }

    @Override
    public MemberPositionViewDTO findById(String id) {
        return memberPositionViewMapper.memberPositionViewEntityToDTO(
                memberPositionViewRepository.findMemberPositionViewEntityById(id));
    }

    @Override
    public MemberPositionViewDTO update(MemberPositionViewDTO memberPositionViewDTO) {
        return memberPositionViewMapper.memberPositionViewEntityToDTO(
                memberPositionViewRepository.saveAndFlush(
                        memberPositionViewMapper.memberPositionViewDTOToEntity(memberPositionViewDTO)));
    }
}
