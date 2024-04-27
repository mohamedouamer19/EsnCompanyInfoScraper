package org.free.work.web.scrapper.core;

import org.free.work.web.scrapper.model.Company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EsnCompanyInfoExtractor {
    private String urlString;

    public EsnCompanyInfoExtractor(String urlString) {
        this.urlString = urlString;
    }

    public List<Company> extractJobListings() throws IOException {
        URL url = new URL(this.urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");

        if (conn.getResponseCode() != 200) {
            throw new IOException("HTTP request failed with code " + conn.getResponseCode());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder htmlContent = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            htmlContent.append(inputLine);
        }
        in.close();
        conn.disconnect();

        Document doc = Jsoup.parse(htmlContent.toString());
        Elements divElements = doc.select("div.space-y-4");
        List<Company> companies = new ArrayList<>();
        for (Element divElement : divElements) {
            Elements aElements = divElement.select("a");
            for (Element aElement : aElements) {
                String hrefAttr = aElement.attr("href");
                String href;
                if (hrefAttr.startsWith("http://") || hrefAttr.startsWith("https://")) {
                    href = hrefAttr;
                } else {
                    href = "https://www.free-work.com" + hrefAttr;
                }
                String companyName = aElement.select("h2").first() != null ? aElement.select("h2").first().text() : null;
                String companySize = aElement.select("div.flex.items-center.mr-4 span.flex-1").first() != null ? aElement.select("div.flex.items-center.mr-4 span.flex-1").first().text() : null;
                String companyType = aElement.select("div.flex.items-center.mr-4 span.flex-1").last() != null ? aElement.select("div.flex.items-center.mr-4 span.flex-1").last().text() : null;
                String companyDescription = aElement.select("div.line-clamp-4").first() != null ? aElement.select("div.line-clamp-4").first().text() : null;

                String websiteLink = "";
                Document companyPageDoc = Jsoup.connect(href).get();
                Element websiteDiv = companyPageDoc.selectFirst("div.inline-flex.items-center");
                if (websiteDiv != null && websiteDiv.selectFirst("a[href]") != null){
                    Element websiteLinkElement = websiteDiv.selectFirst("a[href]");
                    if (websiteLinkElement != null) {
                        websiteLink = websiteLinkElement.attr("href");
                    }
                }

                if (href != null && companyName != null && companySize != null && companyType != null && companyDescription != null) {
                    Company company = new Company(href, companyName, companySize, companyType, companyDescription, websiteLink);
                    companies.add(company);
                }
            }
        }
        return companies;
    }

    public static void main(String[] args) throws IOException {
        List<Company> allCompanies = new ArrayList<>();

        for (int i = 1; i <= 42; i++) {
            String urlString = "https://www.free-work.com/fr/companies/s/esn?activity=9&page=" + i;
            EsnCompanyInfoExtractor extractor = new EsnCompanyInfoExtractor(urlString);
            allCompanies.addAll(extractor.extractJobListings());
        }

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(allCompanies);
        Files.write(Paths.get("src/main/java/org/free/work/web/scrapper/data/companies.json"), json.getBytes());
    }
}