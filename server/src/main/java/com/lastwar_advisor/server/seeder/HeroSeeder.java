package com.lastwar_advisor.server.seeder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastwar_advisor.server.entity.Hero;
import com.lastwar_advisor.server.entity.Skill;
import com.lastwar_advisor.server.entity.SkillEffect;
import com.lastwar_advisor.server.entity.StatKey;
import com.lastwar_advisor.server.repository.HeroRepository;
import com.lastwar_advisor.server.repository.SkillEffectRepository;
import com.lastwar_advisor.server.repository.SkillRepository;
import com.lastwar_advisor.server.repository.StatKeyRepository;

@Component
public class HeroSeeder {
    private final HeroRepository heroRepository;
    private final SkillRepository skillRepo;
    private final StatKeyRepository statKeyRepo;
    private final SkillEffectRepository skillEffectRepo;

    public HeroSeeder(HeroRepository heroRepository, SkillRepository skillRepo, StatKeyRepository statKeyRepo,
            SkillEffectRepository skillEffectRepo) {
        this.heroRepository = heroRepository;
        this.skillRepo = skillRepo;
        this.statKeyRepo = statKeyRepo;
        this.skillEffectRepo = skillEffectRepo;
    }

    public void seedHeroes() throws Exception {
        Set<String> existingHeroes = heroRepository.findAll().stream().map(Hero::getName)
                .collect(java.util.stream.Collectors.toSet());
        Set<String> existingSkills = skillRepo.findAllWithEffects().stream()
                .map(s -> s.getHero().getName() + ":" + s.getName())
                .collect(java.util.stream.Collectors.toSet());
        Set<String> existingEffects = skillEffectRepo.findAllWithRelations().stream()
                .map(e -> e.getSkill().getHero().getName() + ":" + e.getSkill().getName() + ":"
                        + e.getStatKey().getKey() + ":" + e.getLevel())
                .collect(java.util.stream.Collectors.toSet());

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("heroes.json");

        if (is == null) {
            System.out.println("heroes.json not found in classpath, skipping hero seed.");
            return;
        }

        JsonNode heroes = mapper.readTree(is);

        Map<String, Long> statKeyMap = statKeyRepo.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(StatKey::getKey, StatKey::getId));

        for (JsonNode heroData : heroes) {
            if (existingHeroes.contains(heroData.get("name").asText())) { // Skip if hero already exists
                continue;
            }
            Hero hero = new Hero();
            hero.setName(heroData.get("name").asText());
            hero.setRank(heroData.get("rank").asText());
            hero.setType(heroData.get("type").asText());
            hero.setTier(heroData.get("tier").asText());
            hero.setPower(heroData.get("power").asInt());
            hero.setAtk(heroData.get("atk").asInt());
            hero.setDef(heroData.get("def").asInt());
            hero.setHp(heroData.get("hp").asInt());
            hero.setSpd(heroData.get("spd").asInt());
            heroRepository.save(hero);

            System.out.println("→ " + hero.getName());

            JsonNode skills = heroData.get("skills");
            if (skills == null || skills.isEmpty())
                continue;

            for (JsonNode skillData : skills) {
                if (existingSkills.contains(hero.getName() + ":" + skillData.get("name").asText())) { // Skip if skill
                                                                                                      // already exists
                    continue;
                }
                Skill skill = new Skill();
                skill.setHero(hero);
                skill.setType(skillData.get("type").asText());
                skill.setName(skillData.get("name").asText());
                skill.setPriority(skillData.get("priority").asInt());
                skill.setDescription(skillData.get("description").asText());
                skill.setTarget(skillData.get("target").asText());

                JsonNode cooldown = skillData.get("cooldown_s");
                skill.setCooldown(cooldown.isNull() ? null : cooldown.asDouble());

                JsonNode damageType = skillData.get("damage_type");
                skill.setDamageType(damageType.isNull() ? null : damageType.asText());

                List<String> keyStats = new ArrayList<>();
                for (JsonNode ks : skillData.get("key_stats")) {
                    keyStats.add(ks.asText());
                }
                skill.setKeyStats(keyStats);

                skillRepo.save(skill);

                System.out.println("  ✦ " + skill.getName());

                JsonNode scaling = skillData.get("scaling");
                if (scaling == null)
                    continue;

                Map<String, Integer> levelMap = Map.of("lvl1", 1, "lvl20", 20, "lvl40", 40);

                for (Map.Entry<String, Integer> levelEntry : levelMap.entrySet()) {

                    JsonNode levelData = scaling.get(levelEntry.getKey());
                    if (levelData == null)
                        continue;

                    Iterator<Map.Entry<String, JsonNode>> fields = levelData.properties().iterator();
                    ;
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> field = fields.next();
                        String statKeyStr = field.getKey();
                        if (existingEffects // Skip if effect for this skill, stat key, and level already exists
                                .contains(
                                        hero.getName() + ":" + skill.getName() + ":" + statKeyStr + ":"
                                                + levelEntry.getValue())) {
                            continue;
                        }
                        JsonNode val = field.getValue();

                        if (val.isNull())
                            continue;

                        Long statKeyId = statKeyMap.get(statKeyStr);

                        if (statKeyId == null)
                            continue;

                        StatKey statKey = new StatKey();
                        statKey.setId(statKeyId);

                        SkillEffect effect = new SkillEffect();
                        effect.setSkill(skill);
                        effect.setStatKey(statKey);
                        effect.setLevel(levelEntry.getValue());

                        if (val.isBoolean()) {
                            effect.setBoolValue(val.asBoolean());
                        } else {
                            effect.setValue(val.asDouble());
                        }

                        skillEffectRepo.save(effect);
                    }
                }
            }
        }

        System.out.println("Hero seeding complete.");

    }

}
