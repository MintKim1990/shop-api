package portfolio.shopapi.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.shopapi.request.member.MemberDto;
import portfolio.shopapi.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/save")
    public Object save(@RequestBody @Validated MemberDto memberDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(
                    bindingResult.getAllErrors()
                            .stream()
                            .findFirst()
                            .get()
                            .getDefaultMessage()
            );
        } else {
            return memberService.save(memberDto);
        }

    }

}
