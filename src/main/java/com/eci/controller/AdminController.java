package com.eci.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RestController
public class AdminController {
	@Value("${file.path}")
	private String filePath;
	@Value("${filter.folder.path}")
	private String filterFolderPath;
	
	@GetMapping(value = {"","index","home"})
	public String index() {
		return "index";
	}
	
	@PostMapping("/details")
	public String details(String userName,String baseFolderPath,Model m,RedirectAttributes redirectAttributes,HttpSession session) {
		if(userName==null || userName.trim()=="" || baseFolderPath==null || baseFolderPath.trim()=="") {
			redirectAttributes.addFlashAttribute("error","Username and baseFolderPath can't be blank");
			return "redirect:index";
		}
		baseFolderPath=baseFolderPath.trim();
		userName=userName.trim();
		
		session.setAttribute("userName", userName);
		session.setAttribute("baseFolderPath", baseFolderPath);
		
		//Path start = Paths.get(filterFolderPath);
		Path start = Paths.get(baseFolderPath);
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
			List<String> collect = stream.map(String::valueOf).sorted().collect(Collectors.toList());

			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String path : collect) {
				if (path.endsWith(".jpg")) {
					String[] splitArray = path.split(":?\\\\");
					String key = splitArray[splitArray.length - 2];

					List<String> p = Arrays.asList(splitArray);
					List<String> lst = p.subList(p.size() - 5, p.size());
					String relatinvePath = StringUtils.join(lst, '/');

					if (map.containsKey(key)) {
						List<String> strList = map.get(key);
						strList.add(relatinvePath);
						map.put(key, strList);
					} else {
						List<String> strList = new ArrayList<String>();
						strList.add(relatinvePath);
						map.put(key, strList);
					}
//		    		System.out.println(splitArray[splitArray.length-2]+"=>"+path);
				}
			}
			m.addAttribute("list", map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "view";
	}

	@PostMapping("submit")
	public String submit(@RequestParam(defaultValue = "-1")String[] suspense, @RequestParam(defaultValue = "-1")String[] different_images, @RequestParam(defaultValue = "-1")String[] detection_failed,HttpSession session,RedirectAttributes rd) {
		
		System.out.println("S:"+Arrays.toString(suspense));
		System.out.println("DI:"+Arrays.toString(different_images));
		System.out.println("DF:"+Arrays.toString(detection_failed));
		
		String userName=(String) session.getAttribute("userName");
		String baseFolderPath=(String) session.getAttribute("baseFolderPath");
		String []sa = baseFolderPath.split(":?\\\\");
		String dirName =sa[sa.length-1]; 
		
		System.out.println("suspense=>" + suspense.length);
		System.out.println("different_images=>" + different_images.length);
		System.out.println("detection_failed=>" + detection_failed.length);
		
		File suspenseJSONFile = new File(filePath+"/webapps/FilterImages/results/"+userName+"_"+dirName+"_suspense.json");
		File differentJSONFile = new File(filePath+"/webapps/FilterImages/results/"+userName+"_"+dirName+"_different_images.json");
		File detectionFailedJSONFile = new File(filePath+"/webapps/FilterImages/results/"+userName+"_"+dirName+"_detection_failed.json");
		System.out.println("Save Location:"+suspenseJSONFile.getAbsolutePath());
		File f = new File(suspenseJSONFile.getParent());
		if (!f.exists()) {
			System.out.println(f.mkdirs());
		}
		//System.out.println(StringUtils.join(suspense));
		//System.out.println(StringUtils.join(different_images));
		//System.out.println(StringUtils.join(detection_failed));
		
		try {
			FileWriter fw1 = new FileWriter(suspenseJSONFile);
			FileWriter fw2 = new FileWriter(differentJSONFile);
			FileWriter fw3 = new FileWriter(detectionFailedJSONFile);	
			
			fw1.write("["+StringUtils.join(suspense)+"]");
			fw2.write("["+StringUtils.join(different_images)+"]");
			fw3.write("["+StringUtils.join(detection_failed)+"]");
			fw1.close();
			fw2.close();
			fw3.close();
			
			List<String> list = new ArrayList<String>();
			list.add(suspenseJSONFile.getName());
			list.add(detectionFailedJSONFile.getName());
			list.add(differentJSONFile.getName());
			rd.addFlashAttribute("success",list);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:index";
	}

}
