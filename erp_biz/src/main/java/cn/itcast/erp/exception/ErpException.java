package cn.itcast.erp.exception;

/**
 * 自定义异常
 * 作用：终止已知不符合业务逻辑的代码的执行
 *
 */@SuppressWarnings("serial")

 public class ErpException extends RuntimeException {

     public ErpException(String message) {
         super(message);
     }
 }
