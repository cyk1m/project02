package com.fly.pro2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fly.pro2.DAO.HotelDAO;
import com.fly.pro2.DTO.HotelDTO;

@Controller
public class HotelController {
	
	@Autowired
	HotelDAO dao;
	
	@RequestMapping("key.hotel") //키워드에 해당하는 레코드들 검색
	public void find(HotelDTO hotelDTO, Model model) {
		// 주소에 입력한 키워드를 포함하는 레코드들이 list로 return 됨
		List<HotelDTO> list = dao.list(hotelDTO); 
		model.addAttribute("list", list);
		// ajax data로 받은 유저 입력 키워드, 입실&퇴실일자, 객실수, 인원수를
		// model로 views/key.jsp로 전달 (DB에 저장하지 않기 때문에 model 사용)
		model.addAttribute("checkin", hotelDTO.getCheckin()); 
		model.addAttribute("checkout", hotelDTO.getCheckout()); 
		model.addAttribute("stdnum", hotelDTO.getStdnum()); 
		model.addAttribute("guestnum", hotelDTO.getGuestnum()); 
		//System.out.println("key.hotel 조회된 호텔 수>> "+list.size());

		// 전체검색, 모든 이미지 값이 들어있는 컬럼 1개 검색 & 사용하기
		HotelDTO dto = dao.firstrow(hotelDTO);
		String img = dto.getHimage(); // 검색되어 반환된 dto에서 이미지값 꺼내기
		String img2[] = img.split(","); // {램버스1.jpg, 램버스2.jpg, 램버스3.jpg,...}
		// 파일명에 키워드가 포함된 이미지만 list2에 실어서, list 출력하는 forEach문 내 이미지 컬럼에 끼워넣을 것
		ArrayList<String> list2 = new ArrayList<>();
		String key = hotelDTO.getKeyword(); // 유저 입력 키워드
		for (int i = 0; i < img2.length; i++) {
			if (img2[i].contains(key)) {
				//이미지명이 key를 포함하고 있으면 list2에 add
				list2.add(img2[i]); //list2도 키워드 포함이니까 크기는 list와 동일
			} 
		}
		System.out.println("key.hotel 컨트롤러2>> " + list2);
		model.addAttribute("list2", list2); // views/key.jsp로!
	}
	
	//1p 전체검색 평점 정렬
	@RequestMapping("mainlist.hotel")
	public void list(HotelDTO hotelDTO, Model model) { //원본 //dto 입력값 test(9/23)
		List<HotelDTO> list = dao.star();
		model.addAttribute("list", list);
		System.out.println("전체 게시글 수>> " + list.size());
		//test(9/23)
		model.addAttribute("checkin", hotelDTO.getCheckin()); 
		model.addAttribute("checkout", hotelDTO.getCheckout()); 
		model.addAttribute("stdnum", hotelDTO.getStdnum()); 
		model.addAttribute("guestnum", hotelDTO.getGuestnum()); 
	}
	
	//admin계정 권한 - 호텔정보 등록, 수정, 삭제
	@RequestMapping("insert.hotel") //요청주소 1개당 메서드 1개
	public void create(HotelDTO hotelDTO) {
		//컨트롤러의 메서드 내에 선언된 파라메터는 무조건 프로토타입으로 만들어진다.
		System.out.println(hotelDTO);
		dao.create(hotelDTO); //주소가 있으면 메서드 호출 가능
		//리턴으로 특정 jps 호출 안했으니 .fly 절삭하고 
		//views 아래 hotel.jsp를 자동으로 찾아 엶.
	}
	
	@RequestMapping("update.hotel")
	public void update(HotelDTO hotelDTO) {
		System.out.println(hotelDTO);
		dao.update(hotelDTO); 
	}
	
	//stdnum update. 예약한 만큼 잔여객실 -n개
	@RequestMapping("stdupdate.hotel") 
	public void numUp(HotelDTO hotelDTO) {
		System.out.println(hotelDTO);
		//dao.numUpdate(hotelDTO); 
	}
	
	@RequestMapping("delete.hotel") 
	public void delete(HotelDTO hotelDTO) {
		System.out.println(hotelDTO);
		dao.delete(hotelDTO);
	}
	
	//1개 검색 - 호텔 상세정보
	@RequestMapping("search.hotel") 
	public void find2(HotelDTO hotelDTO, Model model) throws ParseException {
//		System.out.println("컨트롤러>> " + hotelDTO.getHid()); //왔는지 찍어보기
		HotelDTO dto = dao.read(hotelDTO);
		model.addAttribute("bag", dto);
		
		//유저입력 투숙 일자, 객실수, 투숙인원
		model.addAttribute("checkin", hotelDTO.getCheckin()); 
		model.addAttribute("checkout", hotelDTO.getCheckout()); 
		model.addAttribute("guestnum", hotelDTO.getGuestnum()); 
		//퇴실-입실일자
		String checkin = hotelDTO.getCheckin() + " 00:00:00"; //시간 안 붙이면 엉뚱한 계산값
		String checkout = hotelDTO.getCheckout() + " 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date inDate = sdf.parse(checkin);
		Date outDate = sdf.parse(checkout);
		 //Date.getTime()은 Date를 밀리세컨드로 변환하여 long형 숫자 데이터로 반환해줌
		long ddd = outDate.getTime() - inDate.getTime();
		long difDate = ddd / (24*60*60*1000); //일자수 차이
		model.addAttribute("diffDays", difDate); 
		//인원수 2명 당 객실 1개 계산
		int guestnum = hotelDTO.getGuestnum();
		int minStdnum = (int) Math.round(guestnum / 2.0);
		model.addAttribute("minStdnum", minStdnum); 
	}
	
	//전체검색 리스트업
//	@RequestMapping("mainlist.hotel")
//	public void list(Model model) {
//		List<HotelDTO> list = dao.list();
//		model.addAttribute("list", list);
//		System.out.println("전체 게시글 수>> " + list.size());
//	}
	
}
