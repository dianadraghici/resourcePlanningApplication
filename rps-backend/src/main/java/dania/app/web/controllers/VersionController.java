package dania.app.web.controllers;

import dania.app.web.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({Constants.VERSION_CONTROLLER})
public class VersionController {

    @Value("${project.groupId}")
    private String groupId;

    @Value("${project.artifactId}")
    private String artifactId;

    @Value("${project.version}")
    private String version;

    @Value("${built.timestamp}")
    private String buildTimestamp;

    @GetMapping
    Map<String, String> getBuildInfo() {
        Map<String, String> infosMap = new HashMap<>();
        infosMap.put("groupId", this.groupId);
        infosMap.put("artifactId", this.artifactId);
        infosMap.put("version", this.version);
        infosMap.put("buildTimestamp", this.buildTimestamp);
        return infosMap;
    }
}
