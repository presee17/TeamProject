package com.mycompany.myapp.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.myapp.dto.Board;
import com.mycompany.myapp.service.BoardService;

@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;

	@RequestMapping("/board/list")
	public String list(@RequestParam(defaultValue = "1") int pageNo, Model model, HttpSession session) {
		logger.info("list()");

		session.setAttribute("pageNo", pageNo);
		// ����¡�� ���� ���� ����
		int rowsPerPage = 10;
		int pagesPerGroup = 5;

		// ��ü �Խù� ��
		int totalBoardNo = boardService.getTotalBoardNo();

		// ��ü ������ ��
		int totalPageNo = totalBoardNo / rowsPerPage;
		if (totalBoardNo % rowsPerPage != 0) {
			totalPageNo++;
		}

		// ��ü �׷� ��
		int totalGroupNo = totalPageNo / pagesPerGroup;
		if (totalPageNo % pagesPerGroup != 0) {
			totalGroupNo++;
		}

		// ���� �׷��ȣ, ������������ȣ, ����������ȣ
		int groupNo = (pageNo - 1) / pagesPerGroup + 1;
		int startPageNo = (groupNo - 1) * pagesPerGroup + 1;
		int endPageNo = startPageNo + pagesPerGroup - 1;
		if (groupNo == totalGroupNo) {
			endPageNo = totalPageNo;
		}

		// ���� ������ �Խù� ����Ʈ
		List<Board> list = boardService.getPage(pageNo, rowsPerPage);
		model.addAttribute("list", list);

		// View�� �ѱ� ������
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);

		
		return "board/list";
	}

	@RequestMapping("/board/writeForm")
	public String writeForm() {
		logger.info("writeForm()");
		return "board/writeForm";
	}

	@RequestMapping("/board/write")
	public String write(Board board, HttpSession session) {
		logger.info("write()");

		// 파일 정보 얻기
		ServletContext application = session.getServletContext();
		String dirpath = application.getRealPath("/resources/uploadfiles");
		String originalFileName = board.getAttach().getOriginalFilename();
		String filesystemName = System.currentTimeMillis() + "-" + originalFileName;
		String contentType = board.getAttach().getContentType();

		if (!board.getAttach().isEmpty()) {
			// 파일에 저장하기
			try {
				board.getAttach().transferTo(new File(dirpath + "/" + filesystemName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			if(!board.getAttach().isEmpty()){
				board.setOriginalFileName(originalFileName);
				board.setFilesystemName(filesystemName);
				board.setContentType(contentType);
			}		
		// 데이터베이스에 게시물 정보 저장
			boardService.add(board);

		return "redirect:/board/list";
	}

	@RequestMapping("/board/detail")
	public String detail(int boardNo, Model model) {
		boardService.addHitcount(boardNo);
		Board board = boardService.getBoard(boardNo);
		model.addAttribute("board", board);
		return "board/detail";
	}

	@RequestMapping("/board/updateForm")
	public String updateForm(int boardNo, Model model) {
		Board board = boardService.getBoard(boardNo);
		model.addAttribute("board", board);
		return "board/updateForm";
	}

	@RequestMapping("/board/update")
	public String update(Board board) {
		boardService.modify(board);
		return "redirect:/board/detail?boardNo=" + board.getNo();
	}

	@RequestMapping("/board/delete")
	public String delete(int boardNo) {
		boardService.remove(boardNo);
		return "redirect:/board/list";
	}
}