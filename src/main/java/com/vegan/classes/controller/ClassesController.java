package com.vegan.classes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vegan.classes.dto.ClassesDTO;
import com.vegan.classes.service.ClassesService;
import com.vegan.mypage.dto.MypageDTO;

@Controller
public class ClassesController {

	private static final Logger logger = LoggerFactory.getLogger(ClassesController.class);

	@Autowired
	ClassesService service;

	/*
	 * @RequestMapping(value = "/class", method = RequestMethod.GET) public String
	 * test(Model model) { logger.info("test"); return "classList"; }
	 */

	@RequestMapping(value = "/classList.go", method = RequestMethod.GET)
	public String ClassList(Model model, HttpSession session) {
		String page = "";
		if (session.getAttribute("loginId") != null) {
			page = "classList";
			String loginId = String.valueOf(session.getAttribute("loginId"));
			int admin = (int) service.adminChk(loginId);
			logger.info("admin" + admin);
			if (admin == 1) {
				model.addAttribute("adminChk", admin);
				if (session.getAttribute("msg") != null) {
					model.addAttribute("msg", session.getAttribute("msg"));

					session.removeAttribute("msg");
				}
			}
		} else {
			page = "login";
			model.addAttribute("msg", "로그인을 해주세요");
		}
		logger.info("list call");
		ArrayList<ClassesDTO> list = service.list();
		logger.info("list cnt : " + list.size());
		System.out.println("list cnt " + list.size());
		model.addAttribute("list", list);

		return page;
	}

	@RequestMapping(value = "/classWrite.go")
	public String ClassWriteForm(Model model) {
		String msg = "";
		model.addAttribute("msg", msg);

		return "classWrite";
	}

	@RequestMapping(value = "/classWrite.do", method = RequestMethod.POST)
	public String ClassWrite(MultipartFile photo, Model model, @RequestParam HashMap<String, String> params,
			HttpSession session) {
		String msg = "클래스 등록";
		logger.info("a" + params);
		model.addAttribute("msg", msg);

		return service.write(photo, params);
	}

	@RequestMapping(value = "/classDetail.do")
	public String detail(Model model, @RequestParam String cl_id, HttpSession session) {
		logger.info("detail : " + cl_id);
		String page = "redirect:/classList.go";
		if (session.getAttribute("msg") != null) {
			String msg = (String) session.getAttribute("msg");
			model.addAttribute("msg", msg);

			session.removeAttribute("msg");
		}
		// 관리자 여부 부분
		if (session.getAttribute("loginId") != null) {
			String loginId = String.valueOf(session.getAttribute("loginId"));
			int admin = (int) service.adminChk(loginId);
			logger.info("admin" + admin);
			if (admin == 1) {
				model.addAttribute("adminChk", admin);
			}
		}
		// 클래스내용부분
		ClassesDTO dto = service.detail(cl_id);

		if (dto != null) {
			page = "classDetail";
			model.addAttribute("dto", dto);
		}
		// 클래스 후기 부분
		ArrayList<ClassesDTO> reviewList = service.reviewList(cl_id);
		model.addAttribute("clReviewList", reviewList);

		return page;
	}

	@RequestMapping(value = "/profile2.do", method = RequestMethod.GET)
	public String profile2(Model model, @RequestParam String user_id, HttpSession session) {
		String page = "login";
		MypageDTO dto = service.profile2(user_id);
		logger.info("dto:" + dto);
		if (dto != null) {
			page = "profile2";
			logger.info("profile2:" + dto);
			model.addAttribute("user", dto);
			// model.addAttribute("cl_id", cl_id);

			String msg = (String) session.getAttribute("msg");
			if (msg != null) {
				model.addAttribute("msg", msg); // 쓰고난 세션은 반드시 바로 삭제해 줘야 한다.
				session.removeAttribute("msg");
			}

		}

		return page;
	}

	@RequestMapping(value = "/class.appWrite.go")
	public String appWriteForm(Model model, @RequestParam String cl_id, HttpSession session) {
		logger.info("클래스 신청 페이지 이동");
		String loginId = String.valueOf(session.getAttribute("loginId"));
		String cl_subject = service.appWrite(cl_id);
		model.addAttribute("cl_id", cl_id);
		model.addAttribute("cl_subject", cl_subject);
		ClassesDTO dto = service.user(loginId);
		model.addAttribute("user", dto);

		return "classAppWrite";
	}

	@RequestMapping(value = "/classAppWrite.do", method = RequestMethod.POST)
	public String appWrite(Model model, @RequestParam HashMap<String, String> params, HttpSession session) {
		logger.info("클래스 신청 저장");
		ClassesDTO dto = new ClassesDTO();

		Random random = new Random();
		int number = random.nextInt(100000000); // 8자리의 숫자 생성
		String cl_part_id = String.valueOf(Math.abs(number)); // 생성된 숫자 출력
		logger.info("참여번호" + cl_part_id);

		int cl_id = Integer.parseInt(params.get("cl_id"));
		dto.setCl_part_id(cl_part_id);
		dto.setCl_id(cl_id);
		dto.setUser_id(params.get("user_id"));
		dto.setPostcode(params.get("postcode"));
		dto.setAddress(params.get("address"));
		dto.setDetailAddress(params.get("detailAddress"));
		dto.setExtraAddress(params.get("extraAddress"));
		dto.setUser_name(params.get("user_name"));
		dto.setUser_phone(params.get("user_phone"));
		int row = service.classApp(dto);
		logger.info("row" + row);
		String page = "";
		if (row == 1) {
			model.addAttribute("dto", dto);
			model.addAttribute("msg", "클래스 신청이 접수되었습니다.");
			page = "classPartNumber";
		} else {
			session.setAttribute("msg", "이미 신청하신 클래스입니다.");
			page = "redirect:/classDetail.do?cl_id=" + cl_id;
		}

		return page;
	}

	@RequestMapping(value = "/class.appList.do")
	public String appList(Model model, @RequestParam String cl_id) {
		logger.info("클래스 신청 리스트");

		logger.info("list call");
		ArrayList<ClassesDTO> appList = service.appList(cl_id);
		logger.info("list cnt : " + appList.size());
		model.addAttribute("appList", appList);

		return "classAppList";
	}

	@RequestMapping(value = "/class.update.go")
	public String updateForm(Model model, @RequestParam String cl_id) {
		logger.info("update : " + cl_id);
		String page = "redirect:/list.do";
		ClassesDTO dto = service.detail(cl_id);
		if (dto != null) {
			page = "classUpdate";
			model.addAttribute("dto", dto);
		}
		return page;
	}

	@RequestMapping(value = "/class.update.do", method = RequestMethod.POST)
	public String update(@RequestParam HashMap<String, String> params) {
		logger.info("params : " + params);
		return service.update(params);
	}

	@RequestMapping(value = "/reviewWrite.do")
	public String reviewWrite(@RequestParam String cl_id, @RequestParam String user_id,
			@RequestParam String review_content, HttpSession session) {
		logger.info("param : " + cl_id + user_id + review_content);
		String page = "";
		int commwrite = service.reviewWrite(cl_id, user_id, review_content);
		if (commwrite == 1) {
			page = "redirect:/classDetail.do?cl_id=" + cl_id;
		}
		return page;
	}

	@RequestMapping(value = "/reviewWrite.ajax")
	@ResponseBody
	public String reviewWrite(@RequestParam HashMap<String, Object> params) {
		logger.info("params : " + params);

		return service.reviewWrite(params);
	}

	@RequestMapping(value = "/reviewDel.do")
	public String commdelete(@RequestParam String review_id, @RequestParam String cl_id, HttpSession session) {

		logger.info("board_id params : " + cl_id);
		logger.info("comment_id params : " + review_id);

		String page = "";
		String loginId = String.valueOf(session.getAttribute("loginId"));
		int row = service.reviewDel(review_id, cl_id, loginId);

		page = "redirect:/classDetail.do?cl_id=" + cl_id;
		return page;
	}

	@RequestMapping(value = "/classDel.do")
	public String classDel(@RequestParam String cl_id, HttpSession session) {

		logger.info("board_id params : " + cl_id);

		String page = "";
		String loginId = String.valueOf(session.getAttribute("loginId"));
		int row = service.classDel(cl_id);
		if (row == 1) {
			page = "redirect:/classList.go";
			session.setAttribute("msg", "클래스가 삭제되었습니다");
		}

		return page;
	}

}