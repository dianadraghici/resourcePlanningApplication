package dania.app.web.integrationTests.ServiceTests;

import dania.app.web.controllers.dto.MemberDTO;
import dania.app.web.integrationTests.config.TestConstants;
import org.springframework.stereotype.Service;

@Service
public class CreateMemberDTOTest {

    public MemberDTO getMemberDTO(Integer id) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(id);
        memberDTO.setComment(TestConstants.COMMENT);
        memberDTO.setFirstName(TestConstants.FIRSTNAME);
        memberDTO.setLastName(TestConstants.LASTNAME);
        memberDTO.setFlag((byte) 1);
        memberDTO.setStaffNumber(TestConstants.STAFF_NUMBER);
        memberDTO.setTechnologyId(TestConstants.ONE_INTEGER);
        memberDTO.setTechnologyDescription(TestConstants.DATA);
        return memberDTO;
    }
}
