package vn.fpt.diamond_shop.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.fpt.diamond_shop.constants.StatusDelivery;
import vn.fpt.diamond_shop.jobs.SendCouponMailJob;
import vn.fpt.diamond_shop.model.Deliver;
import vn.fpt.diamond_shop.model.Delivery;
import vn.fpt.diamond_shop.model.EndUser;
import vn.fpt.diamond_shop.repository.*;
import vn.fpt.diamond_shop.request.ManagerModifyAccountRequest;
import vn.fpt.diamond_shop.response.ManagerListAccountResponse;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.security.model.*;
import vn.fpt.diamond_shop.service.AdminService;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    private SendCouponMailJob sendCouponMailJob;
    @Autowired
    private DeliverRepository deliverRepository;

    @Override
    public void changeInforAccount(ManagerModifyAccountRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (request.getAccountId().equals(userPrincipal.getId())) {
            throw new RuntimeException("Cannot change your own account");
        }

        User user = userRepository.findById(request.getAccountId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setModifiedAt(new Date());
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getFullName() != null) {
            user.setName(request.getFullName());
        }
        if (request.isDeactivate()) {
//            deactivateAccount(request.getAccountId());
            user.setActive(false);
        } else {
//            activateAccount(request.getAccountId());
        }
        if (request.getRoleId() != null) {
            setRole(request.getAccountId(), request.getRoleId());
            if (Objects.equals(request.getRoleId(), RoleEnum.DELIVERY.getId()) && !userRoleRepository.existsById(request.getAccountId())) {
                Deliver delivery = new Deliver();
                delivery.setUserId(request.getAccountId());
                delivery.setStatus(StatusDelivery.StatusDeliver.ACTIVE.getValue());
                delivery.setCreatedAt(new Date());
            }
        }
        userRepository.save(user);
    }

    @Override
    public List<ManagerListAccountResponse> listAccount() {
        List<ManagerListAccountResponse> responses = new ArrayList<>();
        List<User> users = userRepository.findAll();
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        users.forEach(e -> {
            UserRole role = userRoleRepository.findAllByAccountId(e.getId()).get(0);
            RoleEnum roleEnum = RoleEnum.getRoleEnumById(role.getRoleId());
            EndUser endUserByAccountId = endUserRepository.findEndUserByAccountId(e.getId()).get();
            if (!e.getId().equals(userPrincipal.getId())) {
                responses.add(new ManagerListAccountResponse(
                        e.getId(),
                        e.getEmail(),
                        e.getName(),
                        e.getEmailVerified(),
                        roleEnum.name(),
                        e.getProvider().name(),
                        e.isActive(),
                        endUserByAccountId.getPhoneNumber()));
            }
        });
        return responses;
    }

    private void deactivateAccount(Long accountId) {
        User user = userRepository.findById(accountId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        user.setModifiedAt(new Date());
        userRepository.save(user);
    }

    private void activateAccount(Long accountId) {
        User user = userRepository.findById(accountId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        user.setModifiedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public void setRole(Long accountId, Long roleId) {
        List<UserRole> userRoleList = userRoleRepository.findAllByAccountId(accountId);
        if (!userRoleList.isEmpty()) {
            userRoleList.forEach(e -> {
                userRoleRepository.save(new UserRole(e.getId(), accountId, roleId));
            });
            return;
        }

        UserRole role = new UserRole();
        role.setRoleId(roleId);
        role.setAccountId(accountId);
        userRoleRepository.save(role);
    }

    @Override
    public List<EndUser> searchAccount() {
        return endUserRepository.findAll();

    }

    @Override
    public void checkSendMailCoupon() {
        sendCouponMailJob.checkAndSendCoupon();
    }
}
