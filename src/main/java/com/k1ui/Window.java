package com.k1ui;

public class Window {
    public static void main(String[] args) {
        ProcessHandle.allProcesses().forEach(e -> {
            System.out.println("pid: " + e.pid() + ", info: " + e.info());
        });
        System.out.println();
//        WindowUtils.getAllWindows(false);
    }
}
