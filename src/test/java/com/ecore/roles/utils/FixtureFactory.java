package com.ecore.roles.utils;

import static java.util.UUID.fromString;
import java.util.UUID;
import org.assertj.core.util.Lists;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;

public class FixtureFactory {

    public static final UUID UUID_1 = fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID UUID_2 = fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID UUID_3 = fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID UUID_4 = fromString("44444444-4444-4444-4444-444444444444");

    public static final UUID DEVELOPER_ROLE_UUID = fromString("1b3c333b-36e7-4b64-aa15-c22ed5908ce4");
    public static final UUID PRODUCT_OWNER_UUID = fromString("25bbb7d2-26f3-11ec-9621-0242ac130002");
    public static final UUID TESTER_ROLE_UUID = fromString("37969e22-26f3-11ec-9621-0242ac130002");

    public static final UUID GIANNI_USER_UUID = fromString("fd282131-d8aa-4819-b0c8-d9e0bfb1b75c");

    public static final UUID ORDINARY_CORAL_LYNX_TEAM_UUID =
            fromString("7676a4bf-adfe-415c-941b-1739af07039b");

    public static final UUID DEFAULT_MEMBERSHIP_UUID =
            fromString("98de61a0-b9e3-11ec-8422-0242ac120002");

    public static final UUID PO_MEMBERSHIP_UUID =
            fromString("11de61a0-b9e3-11ec-8422-0242ac120002");

    private FixtureFactory() {
        // Default empty constructor
    }

    public static Role getDeveloperRole() {
        return Role.builder()
                .id(DEVELOPER_ROLE_UUID)
                .name("Developer").build();
    }

    public static Role getProductOwnerRole() {
        return Role.builder()
                .id(PRODUCT_OWNER_UUID)
                .name("Product Owner").build();
    }

    public static Role getTesterRole() {
        return Role.builder()
                .id(TESTER_ROLE_UUID)
                .name("Tester").build();
    }

    public static Role getDevOpsRole() {
        return Role.builder()
                .name("DevOps").build();
    }

    public static Team getOrdinaryCoralLynxTeam(boolean full) {
        Team team = Team.builder()
                .id(ORDINARY_CORAL_LYNX_TEAM_UUID)
                .name("System Team").build();
        if (full) {
            team.setTeamLeadId(UUID_1);
            team.setTeamMemberIds(Lists.list(UUID_2, UUID_3, GIANNI_USER_UUID));
        }
        return team;
    }

    public static Team getOrdinaryCoralLynxTeam() {
        return getOrdinaryCoralLynxTeam(true);
    }

    public static User getGianniUser(boolean full) {
        User user = User.builder()
                .id(GIANNI_USER_UUID)
                .displayName("gianniWehner").build();
        if (full) {
            user.setFirstName("Gianni");
            user.setLastName("Wehner");
            user.setAvatarUrl("https://cdn.fakercloud.com/avatars/rude_128.jpg");
            user.setLocation("Brakusstad");
        }
        return user;
    }

    public static User getGianniUser() {
        return getGianniUser(true);
    }

    public static Membership getDefaultMembership() {
        return Membership.builder()
                .id(DEFAULT_MEMBERSHIP_UUID)
                .role(getDeveloperRole())
                .userId(GIANNI_USER_UUID)
                .teamId(ORDINARY_CORAL_LYNX_TEAM_UUID)
                .build();
    }

    public static Membership getProductOwnerMembership() {
        return Membership.builder()
                .id(PO_MEMBERSHIP_UUID)
                .role(getProductOwnerRole())
                .userId(UUID_1)
                .teamId(UUID_3)
                .build();
    }

    public static Membership getInvalidMembership() {
        return Membership.builder()
                .id(DEFAULT_MEMBERSHIP_UUID)
                .role(getDeveloperRole())
                .userId(UUID_4)
                .teamId(ORDINARY_CORAL_LYNX_TEAM_UUID)
                .build();
    }
}
