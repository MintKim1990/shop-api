package portfolio.shopapi.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.shopapi.exception.ParameterException;
import portfolio.shopapi.request.member.MemberSaveRequest;
import portfolio.shopapi.service.member.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/select")
    public Object selectAll() {
        return memberService.findMemberByAll();
    }

    @GetMapping("/select/{memberId}")
    public Object select(@PathVariable(name = "memberId") Long memberId) {
        return memberService.findMemberById(memberId);
    }

    @PostMapping("/save")
    public Object save(@RequestBody @Validated MemberSaveRequest memberSaveRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return memberService.saveMember(memberSaveRequest);
        }
    }

}
