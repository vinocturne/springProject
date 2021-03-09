package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        //ApplicationContesxt, 스프링 컨테이너.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //applicationContext가 AppConfig를 연결시켜주는 스프링 컨테이너로 작용.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        //memberService를 생성하고, applicationContext로 AppConfig 클래스의 memberService를 가져올 수 있도록 설정.

        Member member = new Member(1L, "memberA", Grade.VIP);
        //해당 memberService는 appConfig를 거쳐 생성된 memberService.
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("member : "+ member.getName());
        System.out.println("findMember : "+findMember.getName());

    }
}
