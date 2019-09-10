package com.asiainfo.msooimonitor.service;

import java.util.List;
import java.util.Map;

public interface UploadService {


    List<Map<String,String>> getInterfaceInfo(String sql);
}
