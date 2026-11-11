package com.milamber_brass.brass_armory.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.milamber_brass.brass_armory.BrassArmory;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

public class BrassArmoryBlockModelBuilder implements DataProvider {
    public static final Identifier ROPE_ARROW_TEXTURE = BrassArmory.locate("entity/projectile/rope_arrow");
    private final PackOutput output;

    public BrassArmoryBlockModelBuilder(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var writes = new ArrayList<CompletableFuture<?>>();
        var models = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        writes.add(DataProvider.saveStable(cache, JsonParser.parseString("""
{
  "parent": "minecraft:block/block",
  "elements": [
    {
      "faces": {
        "down": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            14.0,
            10.0,
            16.0
          ]
        },
        "east": {
          "texture": "#rope_texture",
          "uv": [
            8.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "north": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "south": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "up": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            2.0
          ]
        },
        "west": {
          "texture": "#rope_texture",
          "uv": [
            8.0,
            0.0,
            10.0,
            16.0
          ]
        }
      },
      "from": [
        7,
        0,
        13
      ],
      "to": [
        9,
        16,
        15
      ]
    },
    {
      "faces": {
        "down": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            14.0,
            10.0,
            16.0
          ]
        },
        "east": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "north": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "south": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "up": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            2.0
          ]
        },
        "west": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        }
      },
      "from": [
        6,
        11,
        12
      ],
      "to": [
        10,
        15,
        16
      ]
    }
  ],
  "render_type": "minecraft:solid",
  "textures": {
    "particle": "#rope_texture",
    "rope_texture": "brass_armory:block/explorers_rope"
  }
}
"""), models.json(BrassArmory.locate("explorers_rope"))));
        writes.add(DataProvider.saveStable(cache, JsonParser.parseString("""
{
  "parent": "minecraft:block/block",
  "elements": [
    {
      "faces": {
        "down": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            14.0,
            10.0,
            16.0
          ]
        },
        "east": {
          "texture": "#rope_texture",
          "uv": [
            8.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "north": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "south": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            16.0
          ]
        },
        "up": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            2.0
          ]
        },
        "west": {
          "texture": "#rope_texture",
          "uv": [
            8.0,
            0.0,
            10.0,
            16.0
          ]
        }
      },
      "from": [
        7,
        0,
        13
      ],
      "to": [
        9,
        16,
        15
      ]
    },
    {
      "faces": {
        "down": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            14.0,
            10.0,
            16.0
          ]
        },
        "east": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "north": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "south": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        },
        "up": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            0.0,
            10.0,
            2.0
          ]
        },
        "west": {
          "texture": "#rope_texture",
          "uv": [
            6.0,
            12.0,
            10.0,
            16.0
          ]
        }
      },
      "from": [
        6,
        11,
        12
      ],
      "to": [
        10,
        15,
        16
      ]
    },
    {
      "faces": {
        "east": {
          "rotation": 180,
          "texture": "#arrow_texture",
          "uv": [
            0.0,
            0.0,
            8.0,
            2.5
          ]
        },
        "west": {
          "texture": "#arrow_texture",
          "uv": [
            0.0,
            0.0,
            8.0,
            2.5
          ]
        }
      },
      "from": [
        8,
        11,
        5
      ],
      "to": [
        8,
        16,
        21
      ]
    },
    {
      "faces": {
        "down": {
          "rotation": 90,
          "texture": "#arrow_texture",
          "uv": [
            8.0,
            5.0,
            0.0,
            7.5
          ]
        },
        "up": {
          "rotation": 270,
          "texture": "#arrow_texture",
          "uv": [
            8.0,
            2.5,
            0.0,
            0.0
          ]
        }
      },
      "from": [
        5.5,
        13.5,
        5
      ],
      "to": [
        10.5,
        13.5,
        21
      ]
    },
    {
      "faces": {
        "north": {
          "texture": "#arrow_texture",
          "uv": [
            0.0,
            7.5,
            2.5,
            10.0
          ]
        },
        "south": {
          "texture": "#arrow_texture",
          "uv": [
            0.0,
            7.5,
            2.5,
            10.0
          ]
        }
      },
      "from": [
        5.5,
        11,
        6
      ],
      "to": [
        10.5,
        16,
        6
      ]
    }
  ],
  "render_type": "minecraft:cutout",
  "textures": {
    "arrow_texture": "brass_armory:entity/projectile/rope_arrow",
    "particle": "#rope_texture",
    "rope_texture": "brass_armory:block/explorers_rope"
  }
}
"""), models.json(BrassArmory.locate("explorers_rope_arrow"))));

        JsonObject variants = new JsonObject();
        String[] directions = {"north", "east", "south", "west"};
        for (int rotation = 0; rotation < directions.length; rotation++) {
            for (boolean arrow : new boolean[]{false, true}) {
                JsonObject model = new JsonObject();
                model.addProperty("model", "brass_armory:block/explorers_rope" + (arrow ? "_arrow" : ""));
                if (rotation != 0) model.addProperty("y", rotation * 90);
                variants.add("facing=" + directions[rotation] + ",has_arrow=" + arrow, model);
            }
        }
        JsonObject blockState = new JsonObject();
        blockState.add("variants", variants);
        writes.add(DataProvider.saveStable(cache, blockState, output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates").json(BrassArmory.locate("explorers_rope"))));
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Brass Armory block models";
    }
}
