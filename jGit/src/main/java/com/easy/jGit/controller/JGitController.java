package com.easy.jGit.controller;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@Slf4j
public class JGitController {

    /**
     * git仓路径
     */
    final String patch = "/opt/webapps/blog/.git";

    /**
     * 代码分支
     */
    final String branch = "origin/gh-pages";

    /**
     * 拉取
     *
     * @return
     */
    @RequestMapping("/pull")
    public String pull() {
        String result;
        Repository repo = null;
        try {
            repo = new FileRepository(new File(patch));
            Git git = new Git(repo);

            log.info("开始重置");
            //重置
            git.reset()
                    .setMode(ResetCommand.ResetType.HARD)
                    .setRef(branch).call();

            log.info("开始拉取");

            //拉取
            git.pull()
                    .setRemote("origin")
                    .setRemoteBranchName("gh-pages")
                    .call();
            result = "拉取成功!";
            log.info(result);
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            if (repo != null) {
                repo.close();
            }
        }
        return result;
    }

    /**
     * 重置
     *
     * @return
     */
    @RequestMapping("/reset")
    public String reset() {
        String result;

        Repository repo = null;
        try {
            repo = new FileRepository(new File(patch));
            Git git = new Git(repo);
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef(branch).call();
            result = "重置成功!";

        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            if (repo != null) {
                repo.close();
            }
        }
        return result;
    }

    /**
     * 恢复
     */
    @RequestMapping("/revert")
    public String revert() {
        String result;

        Repository repo = null;
        try {
            repo = new FileRepository(new File(patch));
            Git git = new Git(repo);
            git.revert().call();
            result = "恢复成功!";

        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            if (repo != null) {
                repo.close();
            }
        }
        return result;
    }

    /**
     * 克隆
     *
     * @return
     */
    @RequestMapping("/clone")
    public String clone() {
        String result;
        try {
            Git.cloneRepository()
                    .setURI("https://github.com/smltq/blog.git")
                    .setDirectory(new File("/blog"))
                    .call();
            result = "克隆成功了!";
        } catch (GitAPIException e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 状态
     */
    @RequestMapping("/status")
    public static void status() {
        File RepoGitDir = new File("/blog/.git");
        Repository repo = null;
        try {
            repo = new FileRepository(RepoGitDir.getAbsolutePath());
            Git git = new Git(repo);
            Status status = git.status().call();
            log.info("Git Change: " + status.getChanged());
            log.info("Git Modified: " + status.getModified());
            log.info("Git UncommittedChanges: " + status.getUncommittedChanges());
            log.info("Git Untracked: " + status.getUntracked());
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            if (repo != null) {
                repo.close();
            }
        }
    }
}