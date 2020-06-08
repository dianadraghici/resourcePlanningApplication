package dania.app.web.unitTests;

import dania.app.web.controllers.dto.MemberPositionViewDTO;
import dania.app.web.entities.MemberPositionViewEntity;
import dania.app.web.mapper.MemberPositionViewMapper;
import dania.app.web.repository.MemberPositionViewRepository;
import dania.app.web.service.MemberPositionViewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MemberPositionViewServiceTest {

    private MemberPositionViewService memberPositionViewService;
    private MemberPositionViewDTO memberPositionViewDTOExpected;
    private MemberPositionViewEntity memberPositionViewEntityExpected;

    @Mock
    private MemberPositionViewRepository memberPositionViewRepository;
    @Mock
    private MemberPositionViewMapper memberPositionViewMapper;

    @Before
    public void setupMemberPositionViewServiceTest(){

        //given
        memberPositionViewDTOExpected = new MemberPositionViewDTO();
        memberPositionViewDTOExpected.setFirstName("name_test");
        memberPositionViewDTOExpected.setPercentMember("10");
        memberPositionViewDTOExpected.setId("23");

        memberPositionViewEntityExpected = new MemberPositionViewEntity();
        memberPositionViewEntityExpected.setFirstName("entity_name_expected");
        memberPositionViewEntityExpected.setPercentMember("90");
        memberPositionViewEntityExpected.setId("30");

        when(memberPositionViewMapper.memberPositionViewEntityToDTO(any())).thenReturn(memberPositionViewDTOExpected);
        when(memberPositionViewMapper.memberPositionViewDTOToEntity(any())).thenReturn(memberPositionViewEntityExpected);
        when(memberPositionViewMapper.listMemberPositionViewEntityToDTO(any())).thenReturn(Collections.singletonList(memberPositionViewDTOExpected));
        when(memberPositionViewMapper.listMemberPositionViewDTOToEntity(any())).thenReturn(Collections.singletonList(memberPositionViewEntityExpected));

        memberPositionViewService = new MemberPositionViewService(memberPositionViewRepository, memberPositionViewMapper);
    }

    @Test
    public void createMemberPositionViewServiceTest(){
        //given
        when(memberPositionViewRepository.saveAndFlush(any())).thenReturn(memberPositionViewEntityExpected);

        //when
        MemberPositionViewDTO memberPositionViewDTO = memberPositionViewService.create(memberPositionViewDTOExpected);

        //then
        assertEquals(memberPositionViewDTO.getFirstName(), memberPositionViewDTOExpected.getFirstName());
        verify(memberPositionViewRepository).saveAndFlush(memberPositionViewEntityExpected);
    }

    @Test
    public void findByIdMemberPositionViewServiceTest(){
        //given
        setupForFindByIdTest();
        //when
        MemberPositionViewDTO memberPositionViewDTOById = memberPositionViewService.findById("10");

        //then
        assertEquals(memberPositionViewDTOById.getId(), memberPositionViewDTOExpected.getId());
        verify(memberPositionViewRepository).findMemberPositionViewEntityById(anyString());
    }

    @Test
    public void deleteMemberPositionViewServiceTest(){
        // DELETE method from MemberPositionViewService should be refact in the future
        //given
        setupForFindByIdTest();
        when(memberPositionViewService.findById(anyString())).thenReturn(memberPositionViewDTOExpected);

        //when
        MemberPositionViewDTO memberPositionViewDTO = memberPositionViewService.delete("30");

        //then
        assertEquals(memberPositionViewDTO.getFirstName(), memberPositionViewDTOExpected.getFirstName());
        verify(memberPositionViewRepository).delete(memberPositionViewEntityExpected);
        verify(memberPositionViewRepository,times(2)).findMemberPositionViewEntityById(anyString());
    }

    @Test
    public void findAllMemberPositionViewServiceTest(){
        //given
        List<MemberPositionViewEntity> memberPositionViewEntityList = new ArrayList<>();
        memberPositionViewEntityList.add(memberPositionViewEntityExpected);

        //when
        when(memberPositionViewRepository.findAll()).thenReturn(memberPositionViewEntityList);

        //then
        assertEquals(memberPositionViewEntityList.size(), 1);
        assertEquals(memberPositionViewEntityExpected.getId(), memberPositionViewEntityList.get(0).getId());
    }

    @Test
    public void updateMemberPositionViewServiceTest(){
        //given
        memberPositionViewEntityExpected.setFirstName("Ion");
        memberPositionViewDTOExpected.setFirstName("Ion");
        when(memberPositionViewRepository.saveAndFlush(any())).thenReturn(memberPositionViewEntityExpected);

        //when
        MemberPositionViewDTO memberPositionViewDTO = memberPositionViewService.update(memberPositionViewDTOExpected);

        //then
        assertEquals(memberPositionViewDTO.getFirstName(), memberPositionViewDTOExpected.getFirstName());
        verify(memberPositionViewRepository).saveAndFlush(any());
    }

    private void setupForFindByIdTest(){
        when(memberPositionViewRepository.findMemberPositionViewEntityById(anyString())).thenReturn(memberPositionViewEntityExpected);
    }
}
