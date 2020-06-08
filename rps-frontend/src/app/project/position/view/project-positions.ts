import {Component, OnInit, Input} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Project} from "../../../models/project.model";
import {ProjectPosition} from "../../../models/project-position";
import {ProjectService} from "../../project.service";

@Component({
    selector: 'ngbd-project-positions',
    templateUrl: './project-positions.html',
    styleUrls: ['./project-positions.css']
})

export class NgbdProjectPositions implements  OnInit {

    @Input() project: Project;
    projectPositions: { [key: string]: ProjectPosition[]; };
    projectPositionsKeys: string[];
    toggle: any = {};
    errorMessagesArray: string[] = [];
    errorIsOcured: boolean;

    constructor(private modalService: NgbModal,
                private projectService: ProjectService
    ) {

    }

    ngOnInit() {
        this.projectService.getProjectPositions(this.project).subscribe(
            (data) => {
                this.projectPositions = data;
                this.projectPositionsKeys = Object.keys(this.projectPositions);
            });
    }

    open(content: any) {
        this.modalService.open(content, {ariaLabelledBy: 'project-positions-title'}).result.then(() => {
        }).catch(() => {
        });
    }

    close() {
        this.modalService.dismissAll();
    }

    //Remove from this.projectPositions the projectPosition with given projectPosition.id
    removeProjectPosition(projectPosition:ProjectPosition): void {
        let projectPositions = this.projectPositions[projectPosition.positionDescription];
        for(let i=0; i<projectPositions.length; i++){
            if (projectPositions[i].id === projectPosition.id) {
                projectPositions.splice(i,1);
                // if the projectPosition was the last one for that positionDescription,
                // remove the positionDescription too from this.projectPositions and from this.projectPositionsKeys
                if (projectPositions.length == 0) {
                    delete this.projectPositions[projectPosition.positionDescription];
                    this.projectPositionsKeys.splice(this.projectPositionsKeys.indexOf(projectPosition.positionDescription), 1);
                }
                break;
            }
        }
    }


}
