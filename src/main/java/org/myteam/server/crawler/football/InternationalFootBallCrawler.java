package org.myteam.server.crawler.football;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class InternationalFootBallCrawler implements Crawler {

    private static final String URL = "https://m.sports.naver.com/wfootball/schedule/index?date=";

    private static final String LEAGUE_CLASS = "ScheduleAllType_match_list_group__1nFDy";
    private static final String LEAGUE_NAME_CLASS = "ScheduleAllType_title___Qfd4";
    private static final String MATCH_CLASS = "MatchBox_match_item__3_D0Q";
    private static final String MATCH_TIME_CLASS = "MatchBox_time__nIEfd";
    private static final String TEAM_CLASS = "MatchBoxTeamArea_team_item__3w5mq";
    private static final String TEAM_NAME_CLASS = "MatchBoxTeamArea_team__3aB4O";

    private final WebDriver driver;

    public InternationalFootBallCrawler() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드
        this.driver = new ChromeDriver(options);
    }

    @Override
    public void crawl() {
        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver"); // chromedriver 경로 설정

        try {
            driver.get(URL + "2025-01-14");

            // 동적으로 로딩된 경기 일정 추출
            for (WebElement league : getLeague()) {
                String leagueName = getLeagueName(league); // 리그명

                for (WebElement match : getMatch(league)) {
                    String time = getMatchTime(match);

                    List<String> teams = new ArrayList<>();

                    for (WebElement team : getTeam(match)) {
                        teams.add(getTeamName(team)); // 팀명
                    }
                    System.out.println(leagueName);
                    System.out.println(time);
                    System.out.println(teams);
                }
            }
        } finally {
            driver.quit(); // 드라이버 종료
        }
    }

    private List<WebElement> getLeague() {
        return driver.findElements(By.className(LEAGUE_CLASS));
    }

    private String getLeagueName(WebElement league) {
        return league.findElement(By.className(LEAGUE_NAME_CLASS)).getText();
    }

    private List<WebElement> getMatch(WebElement league) {
        return league.findElements(By.className(MATCH_CLASS));
    }

    private String getMatchTime(WebElement match) {
        return match.findElement(By.className(MATCH_TIME_CLASS)).getText();
    }

    private List<WebElement> getTeam(WebElement match) {
        return match.findElements(By.className(TEAM_CLASS));
    }

    private String getTeamName(WebElement team) {
        return team.findElement(By.className(TEAM_NAME_CLASS)).getText();
    }
}
