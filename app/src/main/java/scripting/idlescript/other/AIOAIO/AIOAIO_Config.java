package scripting.idlescript.other.AIOAIO;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import scripting.idlescript.other.AIOAIO.agility.GnomeVillage;
import scripting.idlescript.other.AIOAIO.combat.*;
import scripting.idlescript.other.AIOAIO.fishing.Fish;
import scripting.idlescript.other.AIOAIO.woodcutting.Woodcut;

class AIOAIOConfig {
  public List<AIOAIO_Skill> skills = new ArrayList<>();

  public AIOAIOConfig() {
    skills.add(
        new AIOAIO_Skill(
            "Woodcutting",
            true,
            Arrays.asList(
                new AIOAIO_Task("normal", true, Woodcut::run),
                new AIOAIO_Task("oak", true, Woodcut::run),
                new AIOAIO_Task("willow", true, Woodcut::run))));
    skills.add(
        new AIOAIO_Skill(
            "Fishing",
            true,
            Collections.singletonList(new AIOAIO_Task("Shrimp", true, Fish::run))));
    skills.add(
        new AIOAIO_Skill(
            "Agility",
            true,
            Collections.singletonList(
                new AIOAIO_Task("Tree Gnome Village", true, GnomeVillage::run))));
    skills.add(
        new AIOAIO_Skill(
            "Strength",
            true,
            Arrays.asList(
                new AIOAIO_Task("Lummy Cows", true, Cow::attack),
                new AIOAIO_Task("Draynor Jailguard", true, JailGuard::attack))));
    skills.add(
        new AIOAIO_Skill(
            "Defense",
            true,
            Arrays.asList(
                new AIOAIO_Task("Lummy Cows", true, Cow::attack),
                new AIOAIO_Task("Draynor Jailguard", true, JailGuard::attack))));
    skills.add(
        new AIOAIO_Skill(
            "Attack",
            true,
            Arrays.asList(
                new AIOAIO_Task("Lummy Cows", true, Cow::attack),
                new AIOAIO_Task("Draynor Jailguard", true, JailGuard::attack))));
  }

  public AIOAIO_Skill getRandomEnabledSkill() {
    List<AIOAIO_Skill> enabledSkills =
        skills.stream().filter(AIOAIO_Skill::isEnabled).collect(Collectors.toList());
    if (enabledSkills.isEmpty()) {
      return null;
    }
    int index = ThreadLocalRandom.current().nextInt(enabledSkills.size());
    return enabledSkills.get(index);
  }
}
