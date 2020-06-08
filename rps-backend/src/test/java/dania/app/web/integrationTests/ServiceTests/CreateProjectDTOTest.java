package dania.app.web.integrationTests.ServiceTests;

import dania.app.web.controllers.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dania.app.web.integrationTests.config.TestConstants.*;

@Service
public class CreateProjectDTOTest {

    @Autowired
    private CreateCalendarDTOTest createCalendarDTOTest;
    public CreateProjectDTOTest(CreateCalendarDTOTest createCalendarDTOTest) {
        this.createCalendarDTOTest = createCalendarDTOTest;
    }

    public ProjectDTO getProjectDto() {
        ProjectDTO projectDto = new ProjectDTO();
        projectDto.setId(1);
        projectDto.setProjectCode(PROJECT_CODE);
        projectDto.setProjectName(PROJECT_NAME);
        projectDto.setStartDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());
        projectDto.setEndDateCalendarDTO(createCalendarDTOTest.getCalendarDTO());

        projectDto.setStatusId(1);
        projectDto.setStatusDescription(IN_NEGOTIATION);
        projectDto.setPercentId(5);
        projectDto.setPercentDescription(FIFTY);
        return projectDto;
    }

}
