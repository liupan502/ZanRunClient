package com.example.liupan.zanrunworkclient;

/**
 * Created by liupan on 2017/3/11.
 */

public class EntityJsonExample {

    public static final String PROCEDURES = "{\n" +
            "    code: 0,\n" +
            "    errMsg: ’’,\n" +
            "    data: [\n" +
            "        {\n" +
            "            techuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "            techNo: ”th0000001”,\n" +
            "            techName: ”焊接”\n" +
            "        },\n" +
            "        {\n" +
            "            techuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "            techNo: ”th0000002”,\n" +
            "            techName: ”喷漆”\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String EMPLOYEES = "{\n" +
            "    code: 0,\n" +
            "    errMsg: ’’,\n" +
            "    data: [\n" +
            "        {\n" +
            "            empuuid: aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee,\n" +
            "            empNo: ”emp0000001”,\n" +
            "            empLev: 1,\n" +
            "            empName: ”张三”\n" +
            "        },\n" +
            "        {\n" +
            "            empuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "            empNo: ”emp0000002”,\n" +
            "            empLev: 2,\n" +
            "            empName: ”李四”\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String FLOWCARDS = "{\n" +
            "    code: 0,\n" +
            "    errMsg: ’’,\n" +
            "    data: [\n" +
            "        {\n" +
            "            carduuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "            cardNo: ”c0000001”,\n" +
            "            productName: ”产品1”,\n" +
            "            productNo: ”p0000001”,\n" +
            "            compDate: ”2017-01-01”,\n" +
            "            orderNum: 10000,\n" +
            "            mantiNum: 555,\n" +
            "            techList: [\n" +
            "                {\n" +
            "                    techuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "                    techno: ”th000001”,\n" +
            "                    techReq: ”大小合适，尺寸1”,\n" +
            "                    techNum: 20,\n" +
            "                    qcConfirm: 1\n" +
            "                },\n" +
            "                {\n" +
            "                    techuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "                    techno: ”th000002”,\n" +
            "                    techReq: ”大小合适，尺寸1”,\n" +
            "                    techNum: 30,\n" +
            "                    qcConfirm: 0\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            carduuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "            cardNo: ”c0000002”,\n" +
            "            productName: ”产品2”,\n" +
            "            productNo: ”p0000002”,\n" +
            "            compDate: ”2017-01-01”,\n" +
            "            orderNum: 9999,\n" +
            "            mantiNum: 555,\n" +
            "            techList: [\n" +
            "                {\n" +
            "                    techuuid：123e4567-e89b-12d3-a456-426655440000,\n" +
            "                    techno: ”th000001”,\n" +
            "                    techReq: ”大小合适，尺寸1”,\n" +
            "                    techNum: 10,\n" +
            "                    qcConfirm: 1\n" +
            "                },\n" +
            "                {\n" +
            "                    techuuid: 123e4567-e89b-12d3-a456-426655440000,\n" +
            "                    techno: ”th000003”,\n" +
            "                    techReq: ”大小合适，尺寸2222”,\n" +
            "                    techNum: 10,\n" +
            "                    qcConfirm: 0\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
