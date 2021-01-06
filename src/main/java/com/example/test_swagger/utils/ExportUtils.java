package com.example.test_swagger.utils;

import com.example.test_swagger.commont.Constant;
import com.example.test_swagger.entity.TbBrand;
import com.example.test_swagger.service.KaiService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author shaoqk
 * @create 2020-01-15 15:55
 */
public class ExportUtils {


    private static Logger logger = LoggerFactory.getLogger(ExportUtils.class);


    public static String getExcelTitle(String model, String cycle) {

        return getExcelTitle(model, cycle, Constant.excel2007U);
    }

    public static boolean isNull(@Nullable Object str) {
        return (str == null || "".equals(str.toString().trim()));
    }

    public static String encodingFileName(String fileName, String fileType) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName + fileType, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", " ");
            returnFileName = StringUtils.replace(returnFileName, "%26", "&");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Don't support this encoding ...");
        }
        return returnFileName;
    }

    public static String getExcelTitle(String model, String cycle,
                                       String fileType) {
        if (isNull(model))
            model = Constant.DEFUALT_MODEL_NAME;
        if (isNull(cycle))
            cycle = "";
        StringBuffer sb = new StringBuffer(model);
        sb.append("-");
        sb.append(cycle.trim().replaceAll("/", "").replaceAll(" ", "-"));
        sb.append("-");
        sb.append(new SimpleDateFormat("YYYYMMdd").format(System
                .currentTimeMillis()));
        return encodingFileName(sb.toString(), fileType);
    }


    public static String toZipFiles(HttpServletRequest request, List<String> fileNameList,
                                    String zipFileName) {

        String zipFilePath = request.getRealPath(File.separator + "excel") + File.separator + zipFileName;
        logger.info("begin to create zip file");

        final File[] files = new File[fileNameList.size()];
        for (int i = 0; i < fileNameList.size(); i++) {
            files[i] = new File(fileNameList.get(i));
        }
        // 压缩文件
        final File zipFile = new File(zipFilePath);
        // 将excel文件压缩成zip文件
        final byte[] buf = new byte[1024];
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < files.length; i++) {
                fis = new FileInputStream(files[i]);
                zipOut.putNextEntry(new ZipEntry(files[i].getName()));
                int len = 0;
                while ((len = fis.read(buf)) > 0) {
                    zipOut.write(buf, 0, len);
                }
                zipOut.closeEntry();
                fis.close();
            }
        } catch (final Exception e) {
            zipFilePath = null;
            logger.error("failed to create zip file");
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.close();
                } catch (final IOException e) {
                    logger.error("failed to close ZipOutputStream");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (final IOException e) {
                    logger.error("failed to close FileInputStream");
                }
            }
        }
        return zipFilePath;
    }

    public static void setResponseHeader(final HttpServletResponse response, final String fileName) {

        logger.info("begin to set response header");
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            logger.info("set response header successfully");

        } catch (final Exception e) {
            logger.error("set response header failed", e);
        }
    }

    public static void downloadZip(OutputStream out, String zipFilePath) {

        logger.info("begin to download zip file from " + zipFilePath);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(zipFilePath);
            final byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                out.write(buf, 0, readLength);
            }
            out.flush();
        } catch (final Exception e) {
            logger.error("download zip excel failed");
        } finally {
            try {
                inStream.close();
            } catch (final IOException e) {
                logger.error("failed to close FileInputStream");
            }
            try {
                out.close();
            } catch (final IOException e) {
                logger.error("failed to close OutputStream");
            }
        }
    }

    public static void toExcelOne(HttpServletRequest request,
                                  String fileName, List<String> fileNameList,
                                  KaiService service) {

        logger.info("begin to create excel");
        final File dirFile = new File(request.getRealPath(File.separator + "excel") + File.separator);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        final String file = request.getRealPath(File.separator + "excel") + File.separator + fileName;

        FileOutputStream fos = null;
        XSSFWorkbook wb2 = null;
        try {

            fos = new FileOutputStream(file);
            wb2 = new XSSFWorkbook();
            //创建table工作薄
            XSSFSheet sheets = wb2.createSheet("Summary");
            XSSFRow row2 = null;
            XSSFCell cell2 = null;

            sheets.addMergedRegion(new CellRangeAddress(0, 0, 0, 22));
            sheets.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
            sheets.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
            sheets.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
            sheets.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
            sheets.addMergedRegion(new CellRangeAddress(1, 1, 4, 8));
            sheets.addMergedRegion(new CellRangeAddress(1, 1, 9, 13));
            sheets.addMergedRegion(new CellRangeAddress(1, 1, 14, 16));
            sheets.addMergedRegion(new CellRangeAddress(1, 1, 17, 19));
            sheets.addMergedRegion(new CellRangeAddress(1, 1, 20, 22));


            row2 = sheets.createRow(0);//创建表格行

            createCell(wb2, row2, (short) 0, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "PCA/Revenur/GP variance analysis", (short) 0);

            row2 = sheets.createRow(1);//创建表格行
            createCell(wb2, row2, (short) 0, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "GEO", (short) 0);
            createCell(wb2, row2, (short) 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Region", (short) 0);
            createCell(wb2, row2, (short) 2, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "BPC BU", (short) 0);
            createCell(wb2, row2, (short) 3, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "BU des", (short) 0);
            createCell(wb2, row2, (short) 4, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "2019 Q2", (short) 0);
            createCell(wb2, row2, (short) 5, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 6, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 7, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 8, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 9, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "2019 Q1", (short) 0);
            createCell(wb2, row2, (short) 10, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 11, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 12, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 13, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 14, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "PCA variance analysis", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 15, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 16, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 17, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Revenue variance analysis", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 18, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 19, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 20, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "GP variance analysis", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 21, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 22, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.ROYAL_BLUE.getIndex());

            CellStyle cellStyles = wb2.createCellStyle();
            cellStyles.setWrapText(true);
            cellStyles.setAlignment(HorizontalAlignment.LEFT); // 水平居中  
            cellStyles.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            row2 = sheets.createRow(2);
            createCell(wb2, row2, (short) 0, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 2, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 3, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
            createCell(wb2, row2, (short) 4, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total CA", (short) 0);
            createCell(wb2, row2, (short) 5, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Actual Total CC (daily +O/U) - CPU", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 6, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 7, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC Revenue", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 8, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC BMC GP", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 9, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total CA", (short) 0);
            createCell(wb2, row2, (short) 10, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Actual Total CC (daily +O/U) - CPU", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 11, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 12, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC Revenue", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 13, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC BMC GP", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 14, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 15, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 16, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.SEA_GREEN.getIndex());
            createCell(wb2, row2, (short) 17, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 18, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 19, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.LEMON_CHIFFON.getIndex());
            createCell(wb2, row2, (short) 20, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 21, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.ROYAL_BLUE.getIndex());
            createCell(wb2, row2, (short) 22, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.ROYAL_BLUE.getIndex());

            sheets.addMergedRegion(new CellRangeAddress(4, 4, 0, 2));
            row2 = sheets.createRow(4);
            cell2 = row2.createCell(0);
            cell2.setCellValue("Base on same MTM");


            // List<AnalysisSummary> alist = analysisSummaryService.findByA3(uuid);
            List<TbBrand> all = service.findAll();


            wb2.write(fos);
            fos.flush();
            fileNameList.add(file);

        } catch (final Exception e) {
            logger.error("create excel failed");
        } finally {
            if (wb2 != null) {
                try {
                    wb2.close();
                } catch (final IOException e) {
                    logger.error("workbook关闭失败", e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                    logger.error("输出流关闭失败", e);
                }
            }
        }
    }

    public static void toExcelTwo(HttpServletRequest request,
                                  String fileName, List<String> fileNameList,
                                  KaiService service) {

        logger.info("begin to create excel");
        final File dirFile = new File(request.getRealPath(File.separator + "excel") + File.separator);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        final String file = request.getRealPath(File.separator + "excel") + File.separator + fileName;

        FileOutputStream fos = null;
        XSSFWorkbook wb = null;

        try {
            if (null != service) {

                fos = new FileOutputStream(file);
                wb = new XSSFWorkbook();
                //创建table工作薄
                XSSFSheet sheet = wb.createSheet("ROW");
                XSSFRow row = null;
                XSSFCell cell = null;

                CellRangeAddress cellRangeAddress0 = new CellRangeAddress(0, 1, 0, 0);
                CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 1, 1, 1);
                CellRangeAddress cellRangeAddress2 = new CellRangeAddress(0, 1, 2, 2);
                CellRangeAddress cellRangeAddress3 = new CellRangeAddress(0, 1, 3, 3);
                CellRangeAddress cellRangeAddress4 = new CellRangeAddress(0, 1, 4, 4);
                CellRangeAddress cellRangeAddress5 = new CellRangeAddress(0, 0, 5, 33);
                CellRangeAddress cellRangeAddress6 = new CellRangeAddress(0, 0, 34, 62);
                CellRangeAddress cellRangeAddress7 = new CellRangeAddress(0, 0, 63, 65);
                CellRangeAddress cellRangeAddress8 = new CellRangeAddress(0, 0, 66, 68);
                CellRangeAddress cellRangeAddress9 = new CellRangeAddress(0, 0, 69, 71);


                //添加合并单元格
                sheet.addMergedRegion(cellRangeAddress0);
                sheet.addMergedRegion(cellRangeAddress1);
                sheet.addMergedRegion(cellRangeAddress2);
                sheet.addMergedRegion(cellRangeAddress3);
                sheet.addMergedRegion(cellRangeAddress4);
                sheet.addMergedRegion(cellRangeAddress5);
                sheet.addMergedRegion(cellRangeAddress6);
                sheet.addMergedRegion(cellRangeAddress7);
                sheet.addMergedRegion(cellRangeAddress8);
                sheet.addMergedRegion(cellRangeAddress9);


                row = sheet.createRow(0);//创建表格行
                createCell(wb, row, (short) 0, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "MTM", (short) 0);
                createCell(wb, row, (short) 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "BPC BU", (short) 0);
                createCell(wb, row, (short) 2, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "BU des", (short) 0);
                createCell(wb, row, (short) 3, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "GEO", (short) 0);
                createCell(wb, row, (short) 4, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Region", (short) 0);
                createCell(wb, row, (short) 5, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Current Q", (short) 0);
                createCell(wb, row, (short) 6, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 7, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 8, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 9, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 10, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 11, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 12, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 13, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 14, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 15, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 16, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 17, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 18, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 19, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 20, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 21, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 22, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 23, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 24, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 25, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 26, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 27, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 28, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 29, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 30, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 31, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 32, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 33, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 34, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Last Q", (short) 0);
                createCell(wb, row, (short) 35, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 36, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 37, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 38, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 39, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 40, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 41, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 42, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 43, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 44, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 45, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 46, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 47, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 48, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 49, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 50, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 51, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 52, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 53, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 54, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 55, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 56, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 57, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 58, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 59, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 60, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 61, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 62, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 63, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "PCA", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 64, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 65, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 66, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "Revenue", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 67, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 68, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 69, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "GP", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 70, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 71, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", IndexedColors.ROYAL_BLUE.getIndex());

//                XSSFCellStyle cellStyle2 = wb.createCellStyle();
                CellStyle cellStyle2 = wb.createCellStyle();

                cellStyle2.setWrapText(true);
                cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 水平居中  
                cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

                row = sheet.createRow(1);
                createCell(wb, row, (short) 0, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 2, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 3, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 4, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.CENTER, "", (short) 0);
                createCell(wb, row, (short) 5, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total CA", (short) 0);
                createCell(wb, row, (short) 6, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Invoice qty", (short) 0);
                createCell(wb, row, (short) 7, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "PCA cost", (short) 0);
                createCell(wb, row, (short) 8, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Freight", (short) 0);
                createCell(wb, row, (short) 9, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Tablet Cost Adder", (short) 0);
                createCell(wb, row, (short) 10, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr RCM Rev Matching Cost", (short) 0);
                createCell(wb, row, (short) 11, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Freight O/U", (short) 0);
                createCell(wb, row, (short) 12, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BGOH w/GPN", (short) 0);
                createCell(wb, row, (short) 13, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BGOH w/o GPN", (short) 0);
                createCell(wb, row, (short) 14, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr PM O/U", (short) 0);
                createCell(wb, row, (short) 15, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr PM O/U Allocation", (short) 0);
                createCell(wb, row, (short) 16, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr SW O/U w/GPN", (short) 0);
                createCell(wb, row, (short) 17, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr SW O/U w/GPN", (short) 0);
                createCell(wb, row, (short) 18, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Quarter Cost Credit2", (short) 0);
                createCell(wb, row, (short) 19, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Tablet Cost Adder O/U", (short) 0);
                createCell(wb, row, (short) 20, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Month Non Operational BMC", (short) 0);
                createCell(wb, row, (short) 21, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Non Operational BMC", (short) 0);
                createCell(wb, row, (short) 22, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Other Bucket Cost w/ GPN", (short) 0);
                createCell(wb, row, (short) 23, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Other Bucket Cost w/o GPN", (short) 0);
                createCell(wb, row, (short) 24, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Cost Credit CPU RPU/CAP", (short) 0);
                createCell(wb, row, (short) 25, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Cost Credit CPU O/U - RPU/CAP", (short) 0);
                createCell(wb, row, (short) 26, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Actual Total CC (daily +O/U) - CPU", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 27, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC－- Non-CPU CC", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 28, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 29, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Unit PCA", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 30, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC Revenue", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 31, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Uint Revenue", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 32, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC BMC GP", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 33, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Unit GP", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 34, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total CA", (short) 0);
                createCell(wb, row, (short) 35, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Invoice qty", (short) 0);
                createCell(wb, row, (short) 36, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "PCA cost", (short) 0);
                createCell(wb, row, (short) 37, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Freight", (short) 0);
                createCell(wb, row, (short) 38, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Tablet Cost Adder", (short) 0);
                createCell(wb, row, (short) 39, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr RCM Rev Matching Cost", (short) 0);
                createCell(wb, row, (short) 40, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Freight O/U", (short) 0);
                createCell(wb, row, (short) 41, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BGOH w/GPN", (short) 0);
                createCell(wb, row, (short) 42, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BGOH w/o GPN", (short) 0);
                createCell(wb, row, (short) 43, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr PM O/U", (short) 0);
                createCell(wb, row, (short) 44, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr PM O/U Allocation", (short) 0);
                createCell(wb, row, (short) 45, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr SW O/U w/GPN", (short) 0);
                createCell(wb, row, (short) 46, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr SW O/U w/GPN", (short) 0);
                createCell(wb, row, (short) 47, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Quarter Cost Credit2", (short) 0);
                createCell(wb, row, (short) 48, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Tablet Cost Adder O/U", (short) 0);
                createCell(wb, row, (short) 49, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Month Non Operational BMC", (short) 0);
                createCell(wb, row, (short) 50, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Non Operational BMC", (short) 0);
                createCell(wb, row, (short) 51, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Other Bucket Cost w/ GPN", (short) 0);
                createCell(wb, row, (short) 52, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Other Bucket Cost w/o GPN", (short) 0);
                createCell(wb, row, (short) 53, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Cost Credit CPU RPU/CAP", (short) 0);
                createCell(wb, row, (short) 54, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr Cost Credit CPU O/U - RPU/CAP", (short) 0);
                createCell(wb, row, (short) 55, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Actual Total CC (daily +O/U) - CPU", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 56, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC－- Non-CPU CC", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 57, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "TOTAL Actual BMC", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 58, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Unit PCA", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 59, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC Revenue", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 60, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Uint Revenue", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 61, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Qtr BPC BMC GP", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 62, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Unit GP", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 63, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 64, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 65, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.SEA_GREEN.getIndex());
                createCell(wb, row, (short) 66, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 67, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 68, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.LEMON_CHIFFON.getIndex());
                createCell(wb, row, (short) 69, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Price Variance", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 70, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Quantity/Mix Variance", IndexedColors.ROYAL_BLUE.getIndex());
                createCell(wb, row, (short) 71, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "Total Variance", IndexedColors.ROYAL_BLUE.getIndex());


//                List<TbBrand> list = .findAll(uuid);

            } else {
                Sheet sheet = wb.createSheet("sheet");
                sheet.createRow(0).createCell(0).setCellValue("Temporarily no data！");
            }


            wb.write(fos);
            fos.flush();
            fileNameList.add(file);
        } catch (final Exception e) {
            logger.error("create excel failed");
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (final IOException e) {
                    logger.error("workbook关闭失败", e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (final IOException e) {
                    logger.error("输出流关闭失败", e);
                }
            }
        }
    }

    public static void createCell(XSSFWorkbook wb, XSSFRow row, short column, HorizontalAlignment halign, VerticalAlignment valign, String str, short se) {
        XSSFCell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(str);  // 设置值
        XSSFCellStyle cellStyle = wb.createCellStyle(); // 创建单元格样式
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(halign);  // 设置单元格水平方向对其方式
        cellStyle.setVerticalAlignment(valign); // 设置单元格垂直方向对其方式
        if (se != 0) {
            cellStyle.setFillForegroundColor(se);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cell.setCellStyle(cellStyle); // 设置单元格样式
    }

    private static void createCellDouble(XSSFWorkbook wb, XSSFRow row, short column, HorizontalAlignment halign, VerticalAlignment valign, double dou, short se) {
        XSSFCell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(dou);  // 设置值
        XSSFCellStyle cellStyle = wb.createCellStyle(); // 创建单元格样式
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(halign);  // 设置单元格水平方向对其方式
        cellStyle.setVerticalAlignment(valign); // 设置单元格垂直方向对其方式
        if (se != 0) {
            cellStyle.setFillForegroundColor(se);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cell.setCellStyle(cellStyle); // 设置单元格样式
    }

}
