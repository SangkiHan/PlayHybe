package org.myteam.server.crawler;


import java.io.IOException;

public class BaseballCrawler {

    private static final String BASEBALL_URL = "/baseball";
    private static final String BASEBALL_TITLE_CSS = "strong";
    private static final String BASEBALL_TITLE_CLASS = ".txt";
    private static final String BASEBALL_MAIN_ARTICLE_CSS = ".thumb-news-list.sub-content .thumb-news-list__item";
    private static final String A_TAG = "a";
    private static final String HREF = "href";
    private static final String IMG_TAG = "img";
    private static final String SRC = "src";
    private static final String HTTP = "http";

    public void crawlBaseballArticle(String url) {
//        try {
//            // OSEN 야구 기사 페이지 로드
//            //"https://www.osen.co.kr/baseball"
//            Document doc = Jsoup.connect(url+BASEBALL_URL).get();
//
//            // "content-layout--row sub-content" 클래스 안의 기사 선택
//            Elements mainArticles = doc.select(".content-layout--row.sub-content .thumb");
//
//            for (Element article : mainArticles) {
//                String title;
//                // 첫 번째 기사인지 확인
//                if (!article.select(BASEBALL_TITLE_CSS).isEmpty()) {
//                    title = article.select(BASEBALL_TITLE_CSS).text(); // 제목 (첫 번째 기사)
//                } else {
//                    title = article.select(BASEBALL_TITLE_CLASS).text(); // 제목 (나머지 기사)
//                }
//
//                String articleUrl = article.select(A_TAG).attr(HREF); // 기사 링크
//                String imageUrl = article.select(IMG_TAG).attr(SRC); // 이미지 URL
//
//                // 절대 URL 생성
//                if (!articleUrl.startsWith(HTTP)) {
//                    articleUrl = url + articleUrl; // 상대 URL을 절대 URL로 변환
//                }
//
//                System.out.println("Article - Title: " + title);
//                System.out.println("URL: " + articleUrl);
//                System.out.println("Image URL: " + imageUrl);
//                System.out.println("Detail Content: " + crawlDetailPage(url));
//                System.out.println("-------------------------");
//            }
//
//            // "thumb-news-list sub-content" 클래스 안의 기사 선택
//            Elements subArticles = doc.select(BASEBALL_MAIN_ARTICLE_CSS);
//
//            for (Element article : subArticles) {
//                String title = article.select(BASEBALL_TITLE_CSS).text(); // 기사 제목
//                String articleUrl = article.select(A_TAG).attr(HREF); // 기사 링크
//                String imageUrl = article.select(IMG_TAG).attr(SRC); // 이미지 URL
//
//                // 절대 URL 생성
//                if (!url.startsWith(HTTP)) {
//                    articleUrl = url + articleUrl; // 상대 URL을 절대 URL로 변환
//                }
//
//                System.out.println("Top Article - Title: " + title);
//                System.out.println("URL: " + articleUrl);
//                System.out.println("Image URL: " + imageUrl);
//                System.out.println("-------------------------");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    private String crawlDetailPage(String url) {
//        try {
//            Document detailDoc = Jsoup.connect(url).get();
//            // 상세 내용 추출 (예: <div class="article-content"> 태그 내의 내용 가져오기)
//            Element contentElement = detailDoc.getElementById("articleBody");
//            return contentElement != null ? contentElement.text() : "내용 없음"; // 내용 반환
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "상세 페이지 크롤링 실패";
//        }
//    }
//
//    private String crawlDetailText(String url) {
//        StringBuilder content = new StringBuilder();
//        try {
//            Document detailDoc = Jsoup.connect(url).get();
//
//            // 여러 개의 articleBody가 있을 경우 처리
//            Elements contentElements = detailDoc.select("#articleBody");
//
//            for (Element contentElement : contentElements) {
//                content.append(contentElement.outerHtml()); // HTML 내용 추가
//            }
//
//            // 이미지 URL 절대 경로로 변환
//            Elements images = detailDoc.select("#articleBody img");
//            for (Element img : images) {
//                String imgUrl = img.attr("src");
//                if (!imgUrl.startsWith("http")) {
//                    imgUrl = "https://www.osen.co.kr" + imgUrl; // 절대 URL 변환
//                }
//                img.attr("src", imgUrl); // 업데이트
//            }
//
//            return content.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "상세 페이지 크롤링 실패";
//        }
//    }
}
