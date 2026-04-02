package com.devdotdone.ddd.service;

import com.devdotdone.ddd.dao.ProjectDao;
import com.devdotdone.ddd.dao.UserProjectRoleDao;
import com.devdotdone.ddd.dto.project.Project;
import com.devdotdone.ddd.dto.project.ProjectRequest;
import com.devdotdone.ddd.dto.project.ProjectResponse;
import com.devdotdone.ddd.dto.userProjectRole.UserProjectRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceUpdateMemberTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserProjectRoleDao userProjectRoleDao;

    @Mock
    private UserProjectRoleService userProjectRoleService;

    @Mock
    private ProjectMilestoneService projectMilestoneService;

    @Mock
    private ProjectTagService projectTagService;

    // 기타 ProjectService 내부에서 @Autowired로 주입받는 필드 (사용 안 하지만 주입 필요)
    @Mock
    private com.devdotdone.ddd.dao.UsersDao usersDao;
    @Mock
    private com.devdotdone.ddd.dao.ProjectTagDao projectTagDao;
    @Mock
    private com.devdotdone.ddd.dao.ProjectActivityDao projectActivityDao;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private KnowledgeService knowledgeService;

    private Project existingProject;
    private ProjectRequest request;

    @BeforeEach
    void setUp() {
        existingProject = new Project();
        existingProject.setProjectId(1);
        existingProject.setUserId(10);
        existingProject.setProjectTitle("기존 제목");
        existingProject.setProjectContent("기존 내용");
        existingProject.setProjectStartDate(new Date());
        existingProject.setProjectEndDate(new Date());
        existingProject.setProjectCreatedAt(new Date());

        request = new ProjectRequest();
        request.setProjectId(1);
        request.setProjectTitle("수정된 제목");
        request.setProjectContent("수정된 내용");
        request.setProjectStartDate(new Date());
        request.setProjectEndDate(new Date());
    }



    // ─────────────────────────────────────────────
    // 멤버 추가 관련 테스트
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("멤버 추가 목록이 있으면 assignUsersToProject가 각 userId마다 MEMBER 역할로 호출된다")
    void update_shouldCallAssign_forEachAddUserId() {
        // given
        request.setAddUserIdList(Arrays.asList(201, 202));

        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(1);
        given(userProjectRoleDao.selectProjectMembers(1)).willReturn(Collections.emptyList());

        // when
        projectService.update(request);

        // then
        then(userProjectRoleService).should(times(1)).assignUsersToProject(1, 201, "MEMBER");
        then(userProjectRoleService).should(times(1)).assignUsersToProject(1, 202, "MEMBER");
    }

    @Test
    @DisplayName("멤버 추가 목록이 null이면 assignUsersToProject는 호출되지 않는다")
    void update_shouldNotCallAssign_whenAddListIsNull() {
        // given
        request.setAddUserIdList(null);

        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(1);
        given(userProjectRoleDao.selectProjectMembers(1)).willReturn(Collections.emptyList());

        // when
        projectService.update(request);

        // then
        then(userProjectRoleService).should(never()).assignUsersToProject(anyInt(), anyInt(), anyString());
    }

    @Test
    @DisplayName("멤버 추가 목록이 빈 리스트면 assignUsersToProject는 호출되지 않는다")
    void update_shouldNotCallAssign_whenAddListIsEmpty() {
        // given
        request.setAddUserIdList(Collections.emptyList());

        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(1);
        given(userProjectRoleDao.selectProjectMembers(1)).willReturn(Collections.emptyList());

        // when
        projectService.update(request);

        // then
        then(userProjectRoleService).should(never()).assignUsersToProject(anyInt(), anyInt(), anyString());
    }

    // ─────────────────────────────────────────────
    // 삭제 후 추가 순서 테스트
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("삭제는 추가보다 먼저 실행된다 (InOrder 검증)")
    void update_shouldDeleteBeforeAdd() {
        // given
        request.setRemoveUserIdList(List.of(101));
        request.setAddUserIdList(List.of(201));

        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(1);
        given(userProjectRoleDao.selectProjectMembers(1)).willReturn(Collections.emptyList());

        var inOrder = inOrder(userProjectRoleService);

        // when
        projectService.update(request);

        // then
        inOrder.verify(userProjectRoleService).deleteUsersFromProject(1, 101);
        inOrder.verify(userProjectRoleService).assignUsersToProject(1, 201, "MEMBER");
    }

    // ─────────────────────────────────────────────
    // 응답값 검증 테스트
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("update 결과에 최종 멤버 ID 목록이 포함된다")
    void update_shouldReturnResponseWithMemberIds() {
        // given
        request.setAddUserIdList(List.of(201));

        UserProjectRole member1 = new UserProjectRole();
        member1.setUserId(10);
        UserProjectRole member2 = new UserProjectRole();
        member2.setUserId(201);

        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(1);
        given(userProjectRoleDao.selectProjectMembers(1)).willReturn(Arrays.asList(member1, member2));

        // when
        ProjectResponse response = projectService.update(request);

        // then
        assertThat(response.getMemberIds()).containsExactlyInAnyOrder(10, 201);
    }

    // ─────────────────────────────────────────────
    // 예외 처리 테스트
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("존재하지 않는 프로젝트 ID로 update 하면 IllegalArgumentException이 발생한다")
    void update_shouldThrow_whenProjectNotFound() {
        // given
        given(projectDao.selectProjectById(1)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> projectService.update(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 프로젝트입니다.");
    }

    @Test
    @DisplayName("프로젝트 제목이 null이면 IllegalArgumentException이 발생한다")
    void update_shouldThrow_whenTitleIsNull() {
        // given
        request.setProjectTitle(null);
        given(projectDao.selectProjectById(1)).willReturn(existingProject);

        // when & then
        assertThatThrownBy(() -> projectService.update(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트 제목은 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("프로젝트 제목이 공백이면 IllegalArgumentException이 발생한다")
    void update_shouldThrow_whenTitleIsBlank() {
        // given
        request.setProjectTitle("   ");
        given(projectDao.selectProjectById(1)).willReturn(existingProject);

        // when & then
        assertThatThrownBy(() -> projectService.update(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("프로젝트 제목은 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("DB 업데이트가 실패하면 RuntimeException이 발생한다")
    void update_shouldThrow_whenDbUpdateFails() {
        // given
        given(projectDao.selectProjectById(1)).willReturn(existingProject);
        given(projectDao.updateProject(any())).willReturn(0);

        // when & then
        assertThatThrownBy(() -> projectService.update(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("프로젝트 수정에 실패했습니다.");
    }
}
