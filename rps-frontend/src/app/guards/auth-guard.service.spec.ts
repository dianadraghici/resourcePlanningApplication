import {AuthGuard} from "./auth-guard.service";
import {RouterStateSnapshot} from "@angular/router";

describe('AuthGuard', () => {
    const mockSessionService = jasmine.createSpyObj(['isAuthenticated']);
    const mockRouter = jasmine.createSpyObj(['navigate']);

    it('should create an instance of AuthGuard', () => {
        //Arrange
        const guard = new AuthGuard(mockSessionService, mockRouter);
        //Assert
        expect(guard).toBeTruthy();
    });

    it('should active the route', () => {
        //Arrange
        const guard: AuthGuard =  new AuthGuard(mockSessionService, mockRouter);
        mockSessionService.isAuthenticated.and.returnValue(true);
        //Act
        const isActivated = guard.canActivate(undefined, undefined);
        //Assert
        expect(isActivated).toBe(true);
    });

    it('should NOT active the route and redirect to /login', () => {
        //Arrange
        const guard: AuthGuard =  new AuthGuard(mockSessionService, mockRouter);
        mockSessionService.isAuthenticated.and.returnValue(false);
        const stateSnapshot = {url:'/projects'};
        //Act
        const isActivated = guard.canActivate(undefined, <RouterStateSnapshot>stateSnapshot);
        //Assert
        expect(isActivated).toBe(false);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/login'], { queryParams: { returnUrl: stateSnapshot.url }});
    });
});
