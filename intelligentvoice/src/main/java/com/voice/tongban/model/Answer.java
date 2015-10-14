package com.voice.tongban.model;

public  class Answer {
        /**
         * type : T
         * text : 人心就像一个容器，装的快乐多了，郁闷自然就少。
         */
        // 答案类型
        private String type;
        // 答案文本
        private String text;

        public void setType(String type) {
            this.type = type;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }
    }