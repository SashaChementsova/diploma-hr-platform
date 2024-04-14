package com.controller.employee;

import com.model.Employee;
import com.model.Interview;
import com.model.Result;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;

@Controller
public class EmployeeInterviewController {
    private final InterviewService interviewService;
    private final EmployeeService employeeService;
    private final ResultService resultService;
    private final CandidateService candidateService;
    private final UserDetailService userDetailService;

    private final PositionTestHasQuestionService positionTestHasQuestionService;
    @Autowired
    public EmployeeInterviewController(PositionTestHasQuestionService positionTestHasQuestionService,InterviewService interviewService, EmployeeService employeeService, ResultService resultService, CandidateService candidateService, UserDetailService userDetailService) {
        this.interviewService = interviewService;
        this.employeeService = employeeService;
        this.resultService = resultService;
        this.candidateService = candidateService;
        this.userDetailService = userDetailService;
        this.positionTestHasQuestionService=positionTestHasQuestionService;
    }
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    @GetMapping("/employee/interviews")
    public String getInterviews(Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        Employee employee=userDetailService.findUserByEmail(getCurrentUsername()).getEmployee();
        model.addAttribute("interviews",interviewService.getActiveInterviews(employee.getInterviewEntities()));
        if(interviewService.getActiveInterviews(employee.getInterviewEntities()).isEmpty()) model.addAttribute("emptiness","empty");
        return "employee/interviewControl/getInterviews.html";
    }

    @GetMapping("/employee/interview/{idInterview}")
    public String getInterview(@PathVariable("idInterview") String idInterview, Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        Interview interview=interviewService.findInterviewById(Integer.parseInt(idInterview));
        model.addAttribute("interview",interview);
        model.addAttribute("idInterview",idInterview);
        model.addAttribute("hr",interview.getTrial().getVacancy().getHr());
        model.addAttribute("candidate",interview.getTrial().getCandidate());
        int res=employeeService.compareDates(new SimpleDateFormat("yyyy-MM-dd").format(interview.getDateAndTime()),new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        if(res==0&&interview.getResult().getPoints()==-1) {
            model.addAttribute("today","today");
        }
        return "employee/interviewControl/getInterview.html";
    }

    @GetMapping("/employee/finishInterview/{idInterview}")
    public String getFinishInterview(@PathVariable("idInterview") String idInterview,Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("finishInterview","finishInterview");
        Interview interview=interviewService.findInterviewById(Integer.parseInt(idInterview));
        model.addAttribute("interview",interview);
        model.addAttribute("idInterview",idInterview);
        model.addAttribute("hr",interview.getTrial().getVacancy().getHr());
        model.addAttribute("candidate",interview.getTrial().getCandidate());
        model.addAttribute("result",new Result());
        return "employee/interviewControl/getInterview.html";
    }

    @PostMapping("/employee/finishInterview/end/{idInterview}")
    public String getFinishInterviewEnd(@PathVariable("idInterview") String idInterview,Result result,Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        model.addAttribute("finishInterview","finishInterview");
        Interview interview=interviewService.findInterviewById(Integer.parseInt(idInterview));
        model.addAttribute("interview",interview);
        model.addAttribute("idInterview",idInterview);
        model.addAttribute("hr",interview.getTrial().getVacancy().getHr());
        model.addAttribute("candidate",interview.getTrial().getCandidate());
        model.addAttribute("result",result);
        if(result==null||result.getFeedback().isEmpty()||result.getFeedback().equals("")){
            model.addAttribute("empty","empty");
            return "employee/interviewControl/getInterview.html";
        }
        Result result1=interview.getResult();
        result1.setPoints(result.getPoints());
        result1.setFeedback(result.getFeedback());
        resultService.addAndUpdateResult(result);
        return "redirect:/employee/interviews";
    }

    @GetMapping("/employee/hr/{idInterview}")
    public String getHr(@PathVariable("idInterview") String idInterview,Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        Interview interview=interviewService.findInterviewById(Integer.parseInt(idInterview));
        model.addAttribute("employee",interview.getTrial().getVacancy().getHr().getUserDetail().getEmployee());
        model.addAttribute("idInterview",idInterview);
        return "employee/interviewControl/getHr.html";
    }

    @GetMapping("/employee/candidate/{idInterview}")
    public String getCandidate(@PathVariable("idInterview") String idInterview,Model model){
        candidateService.checkCandidatesByTestsAndInterview();
        Interview interview=interviewService.findInterviewById(Integer.parseInt(idInterview));
        model.addAttribute("candidate",interview.getTrial().getCandidate());
        model.addAttribute("test",interview.getTrial().getResultTesting().getPositionTest());
        if(interview.getTrial().getResultTesting().getPositionTest().getResult().getPoints()!=-1){
            model.addAttribute("trueInterview","trueInterview");
            model.addAttribute("results",positionTestHasQuestionService.getPositionTestHasQuestionsByPositionTest(interview.getTrial().getResultTesting().getPositionTest()));
        }
        model.addAttribute("idInterview",idInterview);
        return "employee/interviewControl/getCandidate.html";
    }


}
