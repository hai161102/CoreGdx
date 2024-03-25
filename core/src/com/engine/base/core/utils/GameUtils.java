package com.engine.base.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.engine.base.core.maths.Vec2;
import com.engine.base.core.maths.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameUtils {
    public static String generateId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 24;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        if (checkIdExist(generatedString)) return GameUtils.generateId();
        else return generatedString;
    }

    public static boolean checkIdExist(String id) {
        FileHandle fileHandle = Gdx.files.local("data/uuids/uuid.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileHandle.read()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (id.equals(line.trim())) return true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        fileHandle.writeString(id + "\n", true);
        return false;
    }

    @SuppressWarnings("NewApi")
    public static List<Vec2> rayCast(final Vec2 origin,
                                     Vec2 dir,
                                     float maxDistance,final @NotNull List<Vec2> others, float radius) {
        return others.stream().filter(vec2 -> !rayCastCirclePoints(
                origin, dir, maxDistance, vec2, radius
        ).isEmpty()).collect(Collectors.toList());
    }

    public static Vec2 rayCastClosest(Vec2 origin, Vec2 dir, float maxDistance, List<Vec2> others, float radius) {
        return nearest(origin, rayCast(origin, dir, maxDistance, others, radius));
    }

    public static @NotNull List<Vec2> rayCastCirclePoints(
            @NotNull Vec2 origin, @NotNull Vec2 dir, float maxDistance, @NotNull Vec2 center, float radius) {
        Vec2 end = origin.cpy().add(dir.cpy().scl(maxDistance));
        List<Vec2> intersectionPoints = new ArrayList<>();

        float baX = end.x - origin.x;
        float baY = end.y - origin.y;
        float caX = center.x - origin.x;
        float caY = center.y - origin.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return intersectionPoints;
        }

        float tmpSqrt = (float) Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vec2 p1 = new Vec2(origin.x - baX * abScalingFactor1, origin.y - baY * abScalingFactor1);
        if (disc == 0) {
            intersectionPoints.add(p1);
            return intersectionPoints;
        }

        Vec2 p2 = new Vec2(origin.x - baX * abScalingFactor2, origin.y - baY * abScalingFactor2);
        intersectionPoints.add(p1);
        intersectionPoints.add(p2);
        return intersectionPoints;
    }

    @SuppressWarnings("NewApi")
    public static @Nullable Vec2 nearest(Vec2 origin, @NotNull List<Vec2> others) {
        if (others.isEmpty()) return null;
        List<Vec2> list = new ArrayList<>(others);
        list.sort((a, b) -> (int) (a.dst(origin) - b.dst(origin)));
        return list.get(0);
    }

    @SuppressWarnings("NewApi")
    public static @Nullable Vec3 nearest(Vec3 origin, @NotNull List<Vec3> others) {
        if (others.isEmpty()) return null;
        List<Vec3> list = new ArrayList<>(others);
        list.sort((a, b) -> (int) (a.dst(origin) - b.dst(origin)));
        return list.get(0);
    }


}
