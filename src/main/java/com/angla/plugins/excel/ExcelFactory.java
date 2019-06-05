package com.angla.plugins.excel;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.ExcelExporter;
import com.angla.plugins.excel.export.ExcelXExporter;
import com.angla.plugins.excel.export.Exporter;
import com.angla.plugins.excel.inventor.parse.Inventor;
import com.angla.plugins.excel.inventor.parse.impl.ExcelInventor;
import com.angla.plugins.excel.inventor.parse.impl.ExcelXInventor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @program: excel-util
 * @description: 工厂类
 * @author: angla
 * @create: 2018-08-02 17:40
 * @Version 1.0
 **/
public class ExcelFactory {


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    private static <T> Exporter<T> initExporter(List<T> data, ExcelTypeEnum excelEnum,
                                                List<String> columns) throws ParameterException {
        if (CollectionUtils.isEmpty(data)) {
            throw new ParameterException("导出数据不能为空");
        }
        if (ExcelTypeEnum.EXCEL_XLS.equals(excelEnum) && data.size() <= ExcelTypeEnum.EXCEL_XLS.getMaxSize()) {
            //数据量超过xls文件格式最大值时用xlsx文件格式进行导出
            return new ExcelExporter<>(data, columns);
        }
        return new ExcelXExporter<>(data, columns);
    }

    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data) throws ParameterException {
        return initExporter(data, null, null);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, List<String> columns) throws ParameterException {
        return initExporter(data, null, columns);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, ExcelTypeEnum excelEnum) throws ParameterException {
        return initExporter(data, excelEnum, null);
    }


    /**
     * 初始化导入工具，默认失败后直接返回错误
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass) throws InvalidFormatException {
        return initInventor(file, tClass, CheckRuleEnum.BREAK_WHEN_ERROR);
    }


    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass, CheckRuleEnum checkRuleEnum) throws InvalidFormatException {

        String fileHeader = getFileHeader(file);
        if (null == fileHeader || "".equals(fileHeader)) {
            throw new ExcelException("未知文件类型");
        }
        Inventor<T> inventor;
        if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLS.getFileHeader())) {
            inventor = new ExcelInventor<>();
        } else if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLSX.getFileHeader())) {
            OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ);
            inventor = new ExcelXInventor<>(pkg, tClass, checkRuleEnum);
        } else {
            throw new ExcelException("错误的文件类型");
        }
        return inventor;
    }


    /**
     * 初始化导入工具，默认失败后直接返回错误
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass) throws IOException {
        return initInventor(inputStream, tClass, CheckRuleEnum.BREAK_WHEN_ERROR);
    }

    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass, CheckRuleEnum checkRuleEnum)
            throws IOException {

        if (null == inputStream) {
            throw new ExcelEmptyException("空文件流");
        }
        Inventor<T> inventor;
        String fileHeader = getFileHeader(inputStream);
        if (null == fileHeader || "".equals(fileHeader)) {
            throw new ExcelException("未知文件类型");
        }
        if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLS.getFileHeader())) {
            inventor = new ExcelInventor<>();
        } else if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLSX.getFileHeader())) {
            try {
                OPCPackage pkg = OPCPackage.open(inputStream);
                inventor = new ExcelXInventor<>(pkg, tClass, checkRuleEnum);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExcelException("解析异常", e);
            } finally {
                inputStream.close();
            }
        } else {
            throw new ExcelException("错误的文件类型");
        }
        return inventor;
    }


    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Invetor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath,
    Class<T> tClass) throws InvalidFormatException {
        return initInventor(filePath, tClass, CheckRuleEnum.BREAK_WHEN_ERROR);
    }


    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath, Class<T> tClass, CheckRuleEnum checkRuleEnum)
            throws InvalidFormatException {

        if (null == filePath || "".equals(filePath)) {
            throw new ExcelEmptyException("空的文件路径");
        }
        Inventor<T> inventor;
        File file = new File(filePath);
        String fileHeader = getFileHeader(file);
        if (null == fileHeader || "".equals(fileHeader)) {
            throw new ExcelException("未知文件类型");
        }
        if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLS.getFileHeader())) {
            inventor = new ExcelInventor<>();
        } else if (fileHeader.equals(ExcelTypeEnum.EXCEL_XLSX.getFileHeader())) {
            OPCPackage pkg = OPCPackage.open(filePath);
            inventor = new ExcelXInventor<>(pkg, tClass, checkRuleEnum);
        } else {
            throw new ExcelException("错误的文件类型");
        }
        return inventor;
    }


    /**
     * 读取文件头信息获取文件类型
     *
     * @param filePath
     * @return
     */
    private static String getFileHeader(String filePath) {
        String value = null;
        try (FileInputStream is = new FileInputStream(filePath)) {
            value = getByteHeader(is);
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * 读取文件头信息获取文件类型
     *
     * @param inputStream
     * @return
     */
    private static String getFileHeader(InputStream inputStream) {

        String value = null;
        try {
            value = getByteHeader(inputStream);
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * 读取文件头信息获取文件类型
     *
     * @param file
     * @return
     */
    private static String getFileHeader(File file) {
        if (!file.exists()) {
            throw new ExcelException("文件不存在");
        }
        if (file.isDirectory()) {
            throw new ExcelException("不能为目录");
        }
        String value = null;
        try (FileInputStream is = new FileInputStream(file)) {
            value = getByteHeader(is);
        } catch (Exception e) {
        }
        return value;
    }


    private static String getByteHeader(InputStream inputStream) throws IOException {
        String value;
        byte[] b = new byte[4];
        inputStream.read(b, 0, b.length);
        value = bytesToHexString(b);
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

}
